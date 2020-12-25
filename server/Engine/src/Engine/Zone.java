package Engine;

import Dtos.*;
import Engine.generated.SDMDiscount;
import Engine.generated.SDMItem;
import Engine.generated.SDMStore;
import User.*;

import java.text.DecimalFormat;
import java.util.*;

public class Zone implements ZoneInterface {

    private final String zoneName;
    private final User zoneOwner;
    private final Map<Integer, Item> marketItems;
    private final Map<Integer, Store> marketStores;
    private final List<DynamicOrder> dynamicOrders;
    private List<StoreDto> newStores;

    public Zone(List<SDMStore> stores, List<SDMItem> items,String zoneName, User owner) {
        this.marketItems = new HashMap<>();
        this.marketStores = new HashMap<>();
        this.dynamicOrders = new ArrayList<DynamicOrder>();
        this.zoneName = zoneName;
        this.zoneOwner = owner;
        this.newStores = new ArrayList<>();;

        for (SDMItem item : items) {
            Item newItem = new Item(item.getId(), item.getName().trim(), PurchaseType.valueOf(item.getPurchaseCategory()));
            marketItems.put(newItem.getItemId(), newItem);
        }

        for (SDMStore store : stores) {
            List<SDMDiscount> discounts;
            if(store.getSDMDiscounts() == null){
                discounts = null;
            }
            else{
                discounts = store.getSDMDiscounts().getSDMDiscount();
            }
            Store newStore = new Store(store.getId(), store.getName().trim(), store.getLocation(),
                    store.getDeliveryPpk(), store.getSDMPrices().getSDMSell(), marketItems , discounts, zoneOwner.getName());
            marketStores.put(newStore.getId(), newStore);
            ((StoreOwner)zoneOwner).addNewStore(zoneName,newStore);
        }

/*        for (SDMCustomer customer : customers) {
            Customer newCustomer = new Customer(
                    customer.getName().trim(), new MapLocation(customer.getLocation().getX(),customer.getLocation().getY()),customer.getId());
            marketCustomers.put(newCustomer.getId(), newCustomer);
        }*/
    }

    public List<MapLocation> getStoresLocations() {
        ArrayList<MapLocation> storesLocation = new ArrayList<>();
        for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
            storesLocation.add(storeEntry.getValue().getMapLocation());
        }

        return storesLocation;
    }

    public List<StoreDto> getStoresDetails() {
        List<StoreDto> storesDetails = new ArrayList<StoreDto>();
        for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
            storesDetails.add(new StoreDto(storeEntry.getValue()));
        }

        return storesDetails;
    }

    public HashMap<Integer, ItemDto> getAllItems() {
        HashMap<Integer, ItemDto> items = new HashMap<>();
        for (Map.Entry<Integer, Item> itemEntry : marketItems.entrySet()) {
            items.put(itemEntry.getKey(), new ItemDto(itemEntry.getValue()));
        }

        return items;
    }

    public HashMap<Integer, StoreDto> getAllStores() {
        HashMap<Integer, StoreDto> storesDetails = new HashMap<>();
        for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
            storesDetails.put(storeEntry.getKey(), new StoreDto(storeEntry.getValue()));
        }
        return storesDetails;
    }

    public StoreDto getStoreById(int StoreId) {
        return new StoreDto(marketStores.get(StoreId));
    }

    public PurchaseType getItemPurchaseType(int serialNumber) {
        return marketItems.get(serialNumber).getPurchaseType();
    }

    public boolean isItemExistInMarket(int itemSerialNumber) {
        return marketItems.containsKey(itemSerialNumber);
    }

    public boolean isItemSoldByStore(int itemSerialNumber, StoreDto selectedStore) {
        return selectedStore.getItems().containsKey(itemSerialNumber);
    }

    public void addItemToItemsInOrder(List<ItemInOrderDto> currentItemsInOrder, StoreDto selectedStore, int serialOfNewItem, float quantity) {
        float price = selectedStore.getItems().get(serialOfNewItem).getPrice();
        Item selectedItem = marketItems.get(serialOfNewItem);
        currentItemsInOrder.add(new ItemInOrderDto(new ItemInOrder(selectedItem, quantity, price,false)));
    }

    public void makeNewStaticOrder(StaticOrderDto newStaticOrder, Customer customer, StoreOwner storeOwner, String zoneName) {
        List<ItemInOrder> newItems = new ArrayList<>();
        for (ItemInOrderDto item : newStaticOrder.getOrderDetails().getItems()) {
            newItems.add(new ItemInOrder(item, marketItems.get(item.getOrderedItem().getItemId())));
        }
        StaticOrder newOrder = new StaticOrder(newStaticOrder.getOrderDate(), newItems, newStaticOrder.getOrderDetails().getDeliveryCost(),
                newStaticOrder.getOrderDetails().getDistance(),newStaticOrder.getStoreId(),customer.getName(), zoneName, newStaticOrder.getCustomerLocation());
        marketStores.get(newStaticOrder.getStoreId()).addStaticOrder(newOrder);
        float AverageItem = newStaticOrder.getOrderDetails().getItemsCost() / newStaticOrder.getOrderDetails().getNumberOfItems();
        customer.addOrder(newOrder,  newOrder.getOrderDetails().getDeliveryCost() ,AverageItem);
        storeOwner.addOrder(newOrder);
        customer.addSendingPaymentTransaction(newStaticOrder.getOrderDate(), newStaticOrder.getTotalOrderCost(),customer.getCreditBalance(),customer.getCreditBalance() - newStaticOrder.getTotalOrderCost());
        customer.setCreditBalance(customer.getCreditBalance() - newStaticOrder.getTotalOrderCost());
        storeOwner.addReceivingPaymentTransaction(newStaticOrder.getOrderDate(), newStaticOrder.getTotalOrderCost(),storeOwner.getCreditBalance(),storeOwner.getCreditBalance() +  newStaticOrder.getTotalOrderCost());
        storeOwner.setCreditBalance(storeOwner.getCreditBalance() +  newStaticOrder.getTotalOrderCost());
    }

    public StaticOrderDto getStaticOrderSummary(List<ItemFromJsonDto> items, int selectedStoreId, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName) {
        float distance = ZoneInterface.calculateDistance(new MapLocationDto(marketStores.get(selectedStoreId).getMapLocation()),customerLocation);
        List<ItemInOrderDto> itemsToBuy = new ArrayList<>();
        for(ItemFromJsonDto item : items){
            itemsToBuy.add(new ItemInOrderDto(new ItemDto(marketItems.get(item.getItemId())),item.getPrice(),item.getQuantity(),item.isPurchaseWithDiscount()));
        }
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(itemsToBuy, distance, distance*marketStores.get(selectedStoreId).getDeliveryPpk());
        return new StaticOrderDto(orderDetailsDto, selectedStoreId,date,customer.getName(),zoneName,customerLocation);
    }

    public float getDeliveryCost(float distance, float ppk) {
        return (float)Math.round(Float.parseFloat(new DecimalFormat("##.##").format(ppk * distance))*100)/100;
    }

    public void addItemToStore(int storeId, int itemSerialNumber, float price) {
        if (!marketItems.containsKey(itemSerialNumber)) {
            throw new IllegalArgumentException("ERROR. Item with serial number " + itemSerialNumber + " is not exist in the market.");
        }
        if (!marketStores.containsKey(storeId)) {
            throw new IllegalArgumentException("ERROR. Store with id " + storeId + " is not exist in the market.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("ERROR. Price shouldn`t ne negative.");
        }
        marketStores.get(storeId).addItemToStore(itemSerialNumber, price,marketItems);
        marketItems.get(itemSerialNumber).increaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(price);
    }

    public void deleteItemFromStore(int storeID, int itemID) {
        if (marketStores.containsKey(storeID)) {
            if (marketStores.get(storeID).getItems().containsKey(itemID)) {
                if (marketItems.get(itemID).getNumberOfStoresThatSellingThisItem() > 1) {
                    if(marketStores.get(storeID).getItems().size() == 1){
                        throw new IllegalArgumentException("ERROR. This is the only item that being sold by this store, hence cant be removed.");
                    }
                    float itemPrice = marketStores.get(storeID).getItems().get(itemID).getPrice();
                    marketStores.get(storeID).removeItem(itemID);
                    marketItems.get(itemID).decreaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(itemPrice);
                    marketStores.get(storeID).getDiscounts().removeIf(d ->d.getCondition().getItemId() == itemID);
                    List<Discount> discountsToRemove = new ArrayList<>();
                    for(Discount discount : marketStores.get(storeID).getDiscounts()) {
                        for (DiscountOffer offer : discount.getBenefit().offer) {
                            if (offer.itemId == itemID) {
                                discountsToRemove.add(discount);
                                break;
                            }
                        }
                    }
                    marketStores.get(storeID).getDiscounts().removeIf(d->discountsToRemove.contains(d));
                } else {
                    throw new IllegalArgumentException("ERROR. The Product with serial number: " + itemID
                            + " is being sold only by this store, hence can't be removed");
                }
            } else {
                throw new IllegalArgumentException("ERROR. The Product with serial number: " + itemID
                        + " is not sold in this store");
            }
        } else {
            throw new IllegalArgumentException("ERROR. Store with id: " + storeID + " is not exist in the system.");
        }
    }

    public void updateItemPrice(int storeID, int itemID, float price) {
        if (!marketStores.containsKey(storeID)) {
            throw new IllegalArgumentException("ERROR. Store with id: " + storeID + " is not exist in the system.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("ERROR. Price shouldn`t ne negative.");
        }
        if (marketStores.get(storeID).getItems().containsKey(itemID)) {
            marketItems.get(itemID).updateAveragePriceAfterAChangeInPrice(marketStores.get(storeID).getItems().get(itemID).getPrice(), price);
            marketStores.get(storeID).updateItemPrice(itemID, price);
        } else {
            throw new IllegalArgumentException("ERROR. The Product with serial number: " + itemID
                    + "is not sold in this store");
        }
    }

    public StoreDto findingTheCheapestStore(ItemDto item) {
        Store cheapestStore = null;
        for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
            if (cheapestStore == null) {
                if (storeEntry.getValue().getItems().containsKey(item.getItemId())) {
                    cheapestStore = storeEntry.getValue();
                }
            } else {
                if (storeEntry.getValue().getItems().containsKey(item.getItemId())) {
                    if (cheapestStore.getItems().get(item.getItemId()).getPrice() > storeEntry.getValue().getItems().get(item.getItemId()).getPrice()) {
                        cheapestStore = storeEntry.getValue();
                    }
                }
            }
        }
        return new StoreDto(cheapestStore);
    }

    public DynamicOrderDto getDynamicOrderSummary(HashMap<Integer, OrderDetailsDto> itemsToOrder, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName) {
        return new DynamicOrderDto(marketStores, itemsToOrder, date, customer.getName(), customerLocation,zoneName);
    }

    public boolean isEmptyLocation(MapLocationDto userLocation) {
        if(userLocation.getX() <= 50 && userLocation.getX() >0 && userLocation.getY()<=50 && userLocation.getY() >0) {
            for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
                if (userLocation.equals(new MapLocationDto(storeEntry.getValue().getMapLocation()))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public List<DynamicOrderDto> getDynamicOrders() {
        List<DynamicOrderDto> dynamicOrdersDto = new ArrayList<DynamicOrderDto>();
        for (DynamicOrder order : dynamicOrders) {
            dynamicOrdersDto.add(new DynamicOrderDto(order));
        }
        return dynamicOrdersDto;
    }

    public void makeDynamicOrder(DynamicOrderDto newOrder, Customer customer, Map<String, User> users, MapLocationDto customerLocation) {
        DynamicOrder newDynamicOrder = new DynamicOrder(newOrder, marketItems, customer.getName(),zoneName,customerLocation);
        dynamicOrders.add(newDynamicOrder);

        float itemsCost =0,itemsCount=0 , averageItems, deliveryCost = 0 , averageDeliveryCost;
        HashSet<StoreOwner> storesOwners = new HashSet<>();
        for (Map.Entry<Integer, OrderDetailsDto> storeEntry : newOrder.getOrdersFromStores().entrySet()) {
            marketStores.get(storeEntry.getKey()).addDynamicOrder(newDynamicOrder);
            itemsCost += storeEntry.getValue().getItemsCost();
            itemsCount += storeEntry.getValue().getNumberOfItems();
            deliveryCost += storeEntry.getValue().getDeliveryCost();

            StoreOwner storeOwner =  (StoreOwner)users.get(marketStores.get(storeEntry.getKey()).getStoreOwner());
            storeOwner.addReceivingPaymentTransaction(newOrder.getOrderDate(),storeEntry.getValue().getTotalOrderCost(),storeOwner.getCreditBalance(),storeOwner.getCreditBalance() +   storeEntry.getValue().getTotalOrderCost());
            storeOwner.setCreditBalance(storeOwner.getCreditBalance() + storeEntry.getValue().getTotalOrderCost());
            storesOwners.add(storeOwner);
        }
        for(StoreOwner storeOwner : storesOwners){
            storeOwner.addOrder(newDynamicOrder);
        }
        averageItems = itemsCost/itemsCount;
        averageDeliveryCost = deliveryCost/newOrder.getOrdersFromStores().size();
        customer.addOrder(newDynamicOrder, averageDeliveryCost, averageItems);

        customer.addSendingPaymentTransaction(newDynamicOrder.getOrderDate(), newDynamicOrder.getItemsCost() + newDynamicOrder.getDeliveryCost(),customer.getCreditBalance(),customer.getCreditBalance() - (newDynamicOrder.getItemsCost() + newDynamicOrder.getDeliveryCost()));
        customer.setCreditBalance(customer.getCreditBalance() - (newDynamicOrder.getItemsCost() + newDynamicOrder.getDeliveryCost()));
    }

    public List<StaticOrderDto> getAllStaticOrders() {
        List<StaticOrderDto> staticOrders = new ArrayList<StaticOrderDto>();
        for (Map.Entry<Integer, Store> storeEntry : marketStores.entrySet()) {
            for (StaticOrder order : storeEntry.getValue().getStaticOrderHistory()) {
                staticOrders.add(new StaticOrderDto(order));
            }
        }
        return staticOrders;
    }

    public List<OrderDto> getAllOrders() {
        List<OrderDto> allOrders = new ArrayList<OrderDto>();
        for (StaticOrderDto staticOrder : getAllStaticOrders()) {
            allOrders.add(staticOrder);
        }
        for (DynamicOrderDto dynamicOrder : getDynamicOrders()) {
            allOrders.add(dynamicOrder);
        }
        return allOrders;
    }

    /*public void restoreStaticOrderFromFile(StaticOrderDto newOrder, int id) {
        List<ItemInOrder> items = new ArrayList<ItemInOrder>();
        for (ItemInOrderDto item : newOrder.getOrderDetails().getItems()) {
            items.add(new ItemInOrder(item, marketItems.get(item.getOrderedItem().getItemId())));
        }
        StaticOrder newStaticOrder = new StaticOrder(newOrder.getOrderDate(), items,newOrder.getOrderDetails().getDeliveryCost(),
                newOrder.getOrderDetails().getDistance(), newOrder.getStoreId(),marketCustomers.get(newOrder.getCustomer().getId()));
        newStaticOrder.setSerialNumber(id);
        marketStores.get(newOrder.getStoreId()).addStaticOrder(newStaticOrder);
    }*/

   /* public void restoreDynamicOrderFromFile(DynamicOrderDto newOrder,int id){
        DynamicOrder newDynamicOrder = new DynamicOrder(newOrder,marketItems,marketCustomers);
        newDynamicOrder.setSerialNumber(id);
        dynamicOrders.add(newDynamicOrder);
        for(Map.Entry<Integer, OrderDetailsDto> storeEntry : newOrder.getOrdersFromStores().entrySet()){
            marketStores.get(storeEntry.getKey()).addDynamicOrder(newDynamicOrder);
        }
    }*/

    public boolean isStoreExistInMarket(int storeId){
        return marketStores.containsKey(storeId);
    }

    /*public HashMap<Integer, CustomerDto> getAllCustomers() {
        HashMap<Integer, CustomerDto> CustomersDetails = new HashMap<>();
        for (Map.Entry<Integer, Customer> CustomerEntry : marketCustomers.entrySet()) {
            CustomersDetails.put(CustomerEntry.getKey(), new CustomerDto(CustomerEntry.getValue()));
        }
        return CustomersDetails;
    }*/

    public void getPossibleDiscountsFromOneStore (List<ItemInOrderDto> items, StoreDto store, List<DiscountDto> possibleDiscounts){
        for(DiscountDto discount : store.getDiscounts()){
            for(ItemInOrderDto item : items){
                if(item.getItemId() == discount.getCondition().getItemId() &&
                        item.getQuantity() >= discount.getCondition().getQuantity()){
                    float countOfDiscounts;
                    if(item.getItemPurchaseType().equals(PurchaseType.Quantity)) {
                        countOfDiscounts = (int) ((item.getQuantity()) / discount.getCondition().getQuantity());
                    }
                    else {
                        countOfDiscounts = item.getQuantity() / (float)discount.getCondition().getQuantity();
                    }
                    for (int i = 1; i <= countOfDiscounts; i++) {
                        possibleDiscounts.add(discount);
                    }

                }
            }
        }
    }

    public List<DiscountDto> getPossibleDiscountsToDynamicOrder(HashMap<Integer,List<ItemInOrderDto>> itemsToOrder){
        List<DiscountDto> possibleDiscounts = new ArrayList<>();
        for(Map.Entry<Integer, List<ItemInOrderDto>> storeEntry : itemsToOrder.entrySet()) {
            getPossibleDiscountsFromOneStore(storeEntry.getValue(),new StoreDto(marketStores.get(storeEntry.getKey())) ,possibleDiscounts);
        }
        return possibleDiscounts;
    }

    public List<String> getItemsPrices(int storeId){
        List<String> prices = new ArrayList<>();
        for(Map.Entry<Integer,Item> item : marketItems.entrySet()){
            if(marketStores.get(storeId).getItems().containsKey(item.getKey())){
                prices.add(String.valueOf(marketStores.get(storeId).getItems().get(item.getKey()).getPrice()));
            }
            else{
                prices.add("Not being sold by this store");
            }
        }

        return prices;
    }

    public Map<Integer, ItemInStoreDto> getItemsBeingSoldByStore(StoreDto store){
        return store.getItems();
    }

    public Map<Integer, ItemDto> getItemsNotBeingSoldByStore(StoreDto store){
        Map<Integer, ItemDto> itemsNotInStore = new HashMap<>();
        for(Map.Entry<Integer, Item> item : marketItems.entrySet() ){
            if(!(store.getItems().containsKey(item.getKey()))){
                itemsNotInStore.put(item.getKey(), new ItemDto(item.getValue()));
            }
        }
        return itemsNotInStore;
    }

    public void filterItemsByQuantity(List<ItemInMenuDto> items){
        items.removeIf(item -> item.getQuantityTextField().getText().equals("") || item.getQuantityTextField().getText().equals("0"));
    }

    public List<ItemInOrderDto> convertToItemInOrder(List<ItemInMenuDto> items){
        List<ItemInOrderDto> newItemsList = new ArrayList<>();
        for(ItemInMenuDto item : items){
            newItemsList.add(new ItemInOrderDto(item,new ItemDto(marketItems.get(item.getID()))));
        }
        return newItemsList;
    }

    public HashMap<Integer, List<ItemInOrderDto>> findCheapestStoreForEachItems(List<ItemFromJsonDto> items){
        HashMap<Integer, List<ItemInOrderDto>> itemsList = new HashMap<>();
        for(ItemFromJsonDto item : items){
            StoreDto curStore = findingTheCheapestStore(new ItemDto(marketItems.get(item.getItemId())));
            if (!itemsList.containsKey(curStore.getId())) {
                itemsList.put(curStore.getId(), new ArrayList<>());
            }
            item.setPrice(curStore.getItems().get(item.getItemId()).getPrice());
            itemsList.get(curStore.getId()).add(new ItemInOrderDto(new ItemDto(marketItems.get(item.getItemId())),item.getPrice(),item.getQuantity(),item.isPurchaseWithDiscount()));

        }
        return itemsList;
    }

    public MapLocationDto getMaxLocationIndexes(){
        int maxX = 0, maxY =0;
        for(Map.Entry<Integer, Store> store : marketStores.entrySet()){
            if(store.getValue().getMapLocation().getX() >maxX){
                maxX = store.getValue().getMapLocation().getX();
            }
            if(store.getValue().getMapLocation().getY() > maxY){
                maxY = store.getValue().getMapLocation().getY();
            }
        }
        return new MapLocationDto(maxX,maxY);
    }

    /*public CustomerDto getCustomerById(int customerId) {
        return new CustomerDto(marketCustomers.get(customerId));
    }*/

    public StoreDto createNewStore(int id, String name, MapLocationDto location, float deliveryPPK, UserDto storeOwner){
        if (isStoreExistInMarket(id)) {
            throw new IllegalArgumentException("ERROR. a store with ID " + id + " already exist.");
        }
        else if(id < 1){
            throw new IllegalArgumentException("ERROR. a store with ID should be a positive integer.");
        }
        if(name.isEmpty()) {
            throw new IllegalArgumentException("ERROR. store name cant be empty.");
        }
        if (deliveryPPK < 0) {
            throw new IllegalArgumentException("ERROR. delivery price per kilometer cant be negative.");
        }
        if (location.getX() < 1 || location.getX() > 50 || location.getY() < 1 || location.getY() > 50) {
            throw new IllegalArgumentException("ERROR. store location coordination should be between 1 to 50.");
        }
        if(!isEmptyLocation(location)) {
            throw new IllegalArgumentException("ERROR. store location is already taken.");
        }

        return new StoreDto(id,name,deliveryPPK,location,storeOwner);
    }

    public Store addStoreToMarket(StoreDto newStore, List<ItemFromJsonDto> items){
        Store store = new Store(newStore,items,marketItems, newStore.getOwner());
        marketStores.put(newStore.getId(), store);
        newStores.add(new StoreDto(store));
        return store;
    }

    public List<StoreToAddItemDto> getStoresToAddItem(){
        List<StoreToAddItemDto> allStores= new ArrayList<>();
        for(Map.Entry<Integer, Store> store : marketStores.entrySet()){
            allStores.add(new StoreToAddItemDto(store.getValue()));
        }
        return allStores;
    }

    public void addItemToStores(ItemDto itemToAdd, List<StoreToAddItemDto> stores){
        for(StoreToAddItemDto store : stores){
            addItemToStore(store.getId(),itemToAdd.getItemId(), Float.parseFloat(store.getPriceTextField().getText()));
        }
    }

    public void addItemToSdm(ItemDto newItem){
        marketItems.put(newItem.getItemId(), new Item(newItem));
    }

    public String getZoneName() {
        return zoneName;
    }

    public User getZoneOwner() {
        return zoneOwner;
    }

    public int getNumberOfOrders(){
        int orders = dynamicOrders.size();
        for(Map.Entry<Integer,Store> store : marketStores.entrySet()){
            orders+=store.getValue().getStaticOrderHistory().size();
        }

        return orders;
    }

    public float calculateAverageOrderCost(){
        float averageCost = 0;
        int orders = getNumberOfOrders();
        if(orders == 0){
            return 0;
        }
        for(DynamicOrder order : dynamicOrders){
            averageCost+= order.getItemsCost();
        }

        for(Map.Entry<Integer,Store> store : marketStores.entrySet()){
            for(StaticOrder order : store.getValue().getStaticOrderHistory()){
                averageCost += order.getOrderDetails().getItemsCost();
            }
        }

        return (float)Math.round((averageCost/orders)*100)/100;
    }

    public void addNewFeedbackToStore(int storeId,FeedbackDto newFeedback){
        marketStores.get(storeId).addNewFeedback(newFeedback);
    }

    public List<OrderDto> getAllStoreOrders(int storeID){
        return marketStores.get(storeID).getAllOrders();
    }

    public List<FeedbackDto> getStoreOwnerFeedbacks(String storeOwnerName){
        List<FeedbackDto> feedbacks = new ArrayList<>();
        for(Map.Entry<Integer, Store> store : marketStores.entrySet()){
            if(store.getValue().getStoreOwner().equals(storeOwnerName)){
                for(Feedback feedback : store.getValue().getFeedbacks()){
                    feedbacks.add(new FeedbackDto(feedback));
                }
            }
        }
        return feedbacks;
    }

    public List<NewStoreAddedAlert> getNewStoreNotifications(String zoneName) {
        List<NewStoreAddedAlert> newStoreAddedAlerts = new ArrayList<>();
        if (!newStores.isEmpty()) {
            for (StoreDto store : newStores) {
                newStoreAddedAlerts.add(new NewStoreAddedAlert(store,marketItems.size(),zoneName));
            }
        }
        newStores = new ArrayList<>();
        return newStoreAddedAlerts;
    }

    public int getNumberOfItems(){
        return marketItems.size();
    }

    public int getNumberOfStores(){
        return marketStores.size();
    }

    public List<StoreInfoDto> getStoresInfo(){
        List<StoreInfoDto> storesInfo = new ArrayList<>();
        for(Store store : marketStores.values()){
            int numberOfOrders = store.getAllOrders().size();
            float totalPaymentForItems = store.calculatePaymentFromItems();
            storesInfo.add(new StoreInfoDto(store.getId(),store.getName(),store.getStoreOwner(), numberOfOrders,totalPaymentForItems,
                    store.getDeliveryPpk(), store.getPaymentReceivedFromDeliveries(), store.getMapLocation()));
        }

        return  storesInfo;
    }
}