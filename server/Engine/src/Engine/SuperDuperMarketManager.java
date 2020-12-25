package Engine;

import Dtos.*;
import User.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.*;

public class SuperDuperMarketManager implements SDMInterface{
    private final Map<String, ZoneInterface> zones;
    private final UserManager userManager;

    public SuperDuperMarketManager(UserManager userManager) {
        zones = new HashMap<>();
        this.userManager = userManager;
    }


    public void addNewZone(ZoneInterface newZone){
        zones.put(newZone.getZoneName(),newZone);
    }

    public HashMap<String, ZoneDto> getAllZones(){
        HashMap<String,ZoneDto> result = new HashMap<>();
        for(Map.Entry<String,ZoneInterface> zone : zones.entrySet()){
            result.put(zone.getKey(),new ZoneDto(zone.getValue()));
        }

        return result;
    }

    public synchronized void addNewZone(File xmlFile, String zoneOwnerName){
        try {
            ZoneInterface newZone = FileUtils.buildSDMFromFile(xmlFile,userManager.getMarketUsers().get(userManager.getUserByName(zoneOwnerName).getName()));
            if(zones.containsKey(newZone.getZoneName())){
                throw new IllegalArgumentException("ERROR. a zone with the name " + newZone.getZoneName() + " already exist in the market.");
            }
            zones.put(newZone.getZoneName(),newZone);
        } catch (JAXBException ignored) {
        }
    }

    public int getNumberOfOrders(String zoneName){
        return zones.get(zoneName).getNumberOfOrders();
    }

    public float calculateAverageOrderCost(String zoneName){
        return (float)Math.round(zones.get(zoneName).calculateAverageOrderCost() * 100) / 100;
    }

    public void addMoneyToUser(String username, float amount, Date date){
        userManager.addMoneyToUser(username,amount, date);
    }

    public boolean isValidLocation(int xLocation, int yLocation, String zone){
        return zones.get(zone).isEmptyLocation(new MapLocationDto(xLocation,yLocation));
    }

    public float getDeliveryCost(String zoneName , int storeId, MapLocationDto customerLocation){
        ZoneInterface zone = zones.get(zoneName);
        StoreDto store = zone.getAllStores().get(storeId);
        return (float)Math.round(ZoneInterface.calculateDistance(store.getLocation(),customerLocation) * store.getDeliveryPpk()*100)/100;
    }

    public List<DiscountDto> getPossibleDiscounts(String zoneName , StoreDto store, List<ItemInOrderDto> items){
        List<DiscountDto> discounts = new ArrayList<>();
        zones.get(zoneName).getPossibleDiscountsFromOneStore(items,store,discounts);
        return discounts;
    }

    public List<DiscountDto> recalculateAvailableDiscount(List<DiscountDto> availableDiscounts , int discountIndex) {
        HashMap<String,DiscountDto> offerTypes = new HashMap<>();
        for(DiscountDto discount : availableDiscounts){
            if((!offerTypes.containsKey(discount.getName())) && !availableDiscounts.get(discountIndex).getName().equals(discount.getName()) &&
                    availableDiscounts.get(discountIndex).getCondition().getItemId() == discount.getCondition().getItemId()){
                offerTypes.put(discount.getName() ,discount);
            }
        }
        List<DiscountDto> discountToChange = new ArrayList<>();
        for(Map.Entry<String,DiscountDto> discount: offerTypes.entrySet()) {
            double countToRemove = availableDiscounts.get(discountIndex).getCondition().getQuantity() / discount.getValue().getCondition().getQuantity();
            for(int i = 0; i<countToRemove; i++){
                discountToChange.add(discount.getValue());
            }
        }
        return discountToChange;
    }

    public synchronized void makeStaticOrder(StaticOrderDto newStaticOrder , CustomerDto customer, String zoneName){
        User customerToOrder = userManager.getMarketUsers().get(customer.getName());
        int storeId = newStaticOrder.getStoreId();
        String storeOwnerName = zones.get(zoneName).getStoreById(storeId).getOwner();
        User storeOwner = userManager.getMarketUsers().get(storeOwnerName);
        zones.get(zoneName).makeNewStaticOrder(newStaticOrder,(Customer)customerToOrder,(StoreOwner) storeOwner,zoneName);
    }

    public synchronized void makeNewDynamicOrder(DynamicOrderDto newOrder, CustomerDto customer, String zoneName){
        User customerToOrder = userManager.getMarketUsers().get(customer.getName());
        zones.get(zoneName).makeDynamicOrder(newOrder,(Customer)customerToOrder,userManager.getMarketUsers(),newOrder.getCustomerLocation());
    }

    public StaticOrderDto getNewStaticOrderSummary(List<ItemFromJsonDto> items, int selectedStoreId,
                                                   Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName){
        return zones.get(zoneName).getStaticOrderSummary(items, selectedStoreId,date,customer,customerLocation,zoneName);
    }


    public DynamicOrderDto getNewDynamicOrderSummary(HashMap<Integer, OrderDetailsDto> itemsToOrder, Date date,
                                                     CustomerDto customer, MapLocationDto customerLocation, String zoneName){
        return zones.get(zoneName).getDynamicOrderSummary(itemsToOrder,date,customer,customerLocation,zoneName);
    }

    public StoreDto getStoreFromZoneById(int storeId, String zoneName){
        return zones.get(zoneName).getStoreById(storeId);
    }

    public HashMap<Integer, List<ItemInOrderDto>> findCheapestStoreForDynamicOrder(List<ItemFromJsonDto> items, String zoneName){
        return zones.get(zoneName).findCheapestStoreForEachItems(items);
    }

    public void addFeedbackToStore(String zoneName, int storeId,FeedbackDto newFeedback , String storeOwnerName){
        zones.get(zoneName).addNewFeedbackToStore(storeId,newFeedback);
        userManager.addFeedbackTOStoreOwner(storeOwnerName,newFeedback);
    }

    public List<OrderExecutionAlertDto> getNewOrdersNotifications (String userName , int numberOfOrderSeen){
        List<OrderExecutionAlertDto> newOrdersNotification = new ArrayList<>();
        StoreOwnerDto storeOwner = (StoreOwnerDto) userManager.getUserByName(userName);
        if(storeOwner.getOrders().size() > numberOfOrderSeen){
            int numberOfNotification = storeOwner.getOrders().size() - numberOfOrderSeen;
            for(int j = 0; j<numberOfNotification; j++){
                OrderDto curOrder = storeOwner.getOrders().get(storeOwner.getOrders().size() - j - 1);
                if(curOrder instanceof StaticOrderDto){
                    newOrdersNotification.add(new OrderExecutionAlertDto(curOrder.getSerialNumber(), curOrder.getCustomerName(),((StaticOrderDto) curOrder).getOrderDetails()));
                }
                else{
                    for(Map.Entry<Integer, OrderDetailsDto> store : ((DynamicOrderDto)curOrder).getOrdersFromStores().entrySet()){

                        for(StoreDto userStore : storeOwner.getStores().get(curOrder.getZoneName())){
                            if(userStore.getId() == store.getKey()){
                                newOrdersNotification.add(new OrderExecutionAlertDto(curOrder.getSerialNumber(), curOrder.getCustomerName(),store.getValue()));
                            }
                        }
                    }
                }
            }
        }
        return newOrdersNotification;
    }

    public List<FeedbackAlertDto> getNewFeedbackNotifications (String userName , int numberOfFeedbackSeen){
        List<FeedbackAlertDto> newFeedbackNotification = new ArrayList<>();
        StoreOwnerDto storeOwner = (StoreOwnerDto) userManager.getUserByName(userName);
        if(storeOwner.getFeedbacks().size() > numberOfFeedbackSeen){
            int numberOfNotification = storeOwner.getFeedbacks().size() - numberOfFeedbackSeen;
            for(int j = 0; j<numberOfNotification; j++){
                FeedbackDto curFeedback = storeOwner.getFeedbacks().get(storeOwner.getFeedbacks().size() - j - 1);
                newFeedbackNotification.add(new FeedbackAlertDto(curFeedback));
            }
        }
        return newFeedbackNotification;
    }

    public List<AccountTransactionDto> getAllTransactions(String userName){
        UserDto user = userManager.getUserByName(userName);
        return user.getUserTransactions();
    }

    public List<OrderDto> getOrdersByCustomerAndZone(String zoneName, String customerName){
        List<OrderDto> allOrders = new ArrayList<>();
        CustomerDto customer = (CustomerDto)userManager.getUserByName(customerName);
        for(OrderDto order : customer.getOrders()){
            if(order.getZoneName().equals(zoneName)){
                allOrders.add(order);
            }
        }
        return allOrders;
    }

    public OrderDto getOrderById(String zoneName, String customerName, int orderId){
        CustomerDto customer = (CustomerDto)userManager.getUserByName(customerName);
        OrderDto orderToReturn= null;
        for(OrderDto order : customer.getOrders()){
            if(order.getZoneName().equals(zoneName) && order.getSerialNumber() == orderId){
                orderToReturn= order;
            }
        }
        return orderToReturn;
    }

    public List<OrderDto> getOrdersByStore(String zoneName, int storeID){
        return zones.get(zoneName).getAllStoreOrders(storeID);
    }

    public List<FeedbackDto> getStoreOwnerFeedbacksInZone(String zoneName, String storeOwnerName){
        return zones.get(zoneName).getStoreOwnerFeedbacks(storeOwnerName);
    }

    public boolean isStoreIdAvailableInZone(String zoneName, int storeId){
        return !zones.get(zoneName).isStoreExistInMarket(storeId);
    }

    public synchronized void addNewStoreToZone(String zoneName, StoreDto newStore, List<ItemFromJsonDto> items){
        Store store = zones.get(zoneName).addStoreToMarket(newStore, items);
        userManager.addStoreToStoreOwner(store,newStore.getOwner(),zoneName);
    }

    public List<NewStoreAddedAlert> getNewStoreNotificationsFromZone (String userName){
        List<NewStoreAddedAlert> newStoreAddedAlerts = new ArrayList<>();
        for(Map.Entry<String,ZoneInterface> zone : zones.entrySet()){
            if(zone.getValue().getZoneOwner().getName().equals(userName)){
                for(NewStoreAddedAlert alert : zones.get(zone.getKey()).getNewStoreNotifications(zone.getKey())){
                    newStoreAddedAlerts.add(alert);
                }
            }
        }
        return newStoreAddedAlerts;
    }

    public List<ZoneInfoDto> getZonesInfo(){
        List<ZoneInfoDto> zonesInfo = new ArrayList<>();
        for(ZoneInterface zone : zones.values()){
            zonesInfo.add(new ZoneInfoDto(zone.getZoneOwner().getName(),zone.getZoneName(),zone.getNumberOfItems(), zone.getNumberOfStores(),
                    zone.getNumberOfOrders(), calculateAverageOrderCost(zone.getZoneName())));
        }

        return zonesInfo;
    }

    public List<StoreInfoDto> getStoresInfo(String zoneName){
        return zones.get(zoneName).getStoresInfo();
    }
}
