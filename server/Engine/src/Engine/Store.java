package Engine;

import Dtos.*;
import Engine.generated.Location;
import Engine.generated.SDMDiscount;
import Engine.generated.SDMOffer;
import Engine.generated.SDMSell;
import User.*;

import java.util.*;

public class Store {
    private final int id;
    private final String name;
    private final MapLocation mapLocation;
    private final float deliveryPpk;
    private float paymentReceivedFromDeliveries;
    private final String storeOwner;
    private final Map<Integer, ItemInStore> items;
    private final List<StaticOrder> staticOrderHistory;
    private final List<DynamicOrder> DynamicOrderHistory;
    private final List<Discount> discounts;
    private final List<Feedback> feedbacks;

    public Store(StoreDto store, List<ItemFromJsonDto> storeItems, Map<Integer,Item> allItems, String owner){
        id= store.getId();
        storeOwner = owner;
        name=store.getName();
        mapLocation = new MapLocation(store.getLocation());
        deliveryPpk = store.getDeliveryPpk();
        paymentReceivedFromDeliveries = 0;
        items = new HashMap<>();
        staticOrderHistory = new ArrayList<>();
        DynamicOrderHistory = new ArrayList<>();
        discounts = new ArrayList<>();
        feedbacks = new ArrayList<>();
        for(ItemFromJsonDto item : storeItems){
            Item currentItem = allItems.get(item.getItemId());
            items.put(item.getItemId(), new ItemInStore(item.getPrice(),currentItem));
            currentItem.increaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(item.getPrice());
        }
    }

    public Store(int id, String name, Location location, float deliveryPpk, List<SDMSell> soldItems, Map<Integer,Item> allItems, List<SDMDiscount> discounts, String owner){
        this.id = id;
        this.storeOwner = owner;
        this.name = name;
        this.deliveryPpk = deliveryPpk;
        this.mapLocation = new MapLocation(location.getX(),location.getY());
        this.paymentReceivedFromDeliveries = 0;
        this.staticOrderHistory = new ArrayList<>();
        this.DynamicOrderHistory = new ArrayList<>();
        this.discounts = new ArrayList<>();
        feedbacks = new ArrayList<>();
        items = new HashMap<>();
        for(SDMSell item : soldItems){
            Item currentItem = allItems.get(item.getItemId());
            currentItem.increaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(item.getPrice());
            items.put(currentItem.getItemId(),new ItemInStore((float)item.getPrice() ,currentItem));
        }
        if(discounts != null) {
            for (SDMDiscount discount : discounts) {

                DiscountCondition condition = new DiscountCondition(discount.getIfYouBuy().getQuantity(), discount.getIfYouBuy().getItemId());
                ArrayList<DiscountOffer> offers = new ArrayList<>();
                for (SDMOffer offer : discount.getThenYouGet().getSDMOffer()) {
                    offers.add(new DiscountOffer(offer.getQuantity(), offer.getItemId(), offer.getForAdditional() ,items.get(offer.getItemId()).getName() ));
                }
                DiscountBenefit benefit = new DiscountBenefit(offers, discount.getThenYouGet().getOperator());
                this.discounts.add(new Discount(condition, benefit, discount.getName()));
            }
        }
    }

    public List<StaticOrder> getStaticOrderHistory() {
        return staticOrderHistory;
    }

    public List<DynamicOrder> getDynamicOrderHistory() {
        return DynamicOrderHistory;
    }

    protected void addItemToStore(int serialNumber, float price, Map<Integer, Item> marketItems ){
        if(items.containsKey(serialNumber)){
            throw new IllegalArgumentException("ERROR. Item with serial number: " + serialNumber + " already exist in store with id: " + id);
        }
        else{
            items.put(serialNumber,new ItemInStore(price,marketItems.get(serialNumber)));
        }
    }

    protected void removeItem(int itemID){
        items.remove(itemID);
    }

    public Map<Integer, ItemInStore> getItems() {
        return items;
    }

    public float getPaymentReceivedFromDeliveries() {
        return (float)Math.round(paymentReceivedFromDeliveries*100)/100;
    }

    public float getDeliveryPpk() {
        return (float)Math.round(deliveryPpk*100)/100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id;
    }

    public MapLocation getMapLocation() {
        return mapLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public int getId() {
        return id;
    }

    public void addStaticOrder(StaticOrder order) {
        this.staticOrderHistory.add(order);
        this.paymentReceivedFromDeliveries += order.getOrderDetails().getDeliveryCost();
        for(ItemInOrder item : order.getOrderDetails().getItems()){
            items.get(item.getOrderedItem().getItemId()).increaseNumberOfTimeSold(item.getQuantity());
        }
    }

    protected void updateItemPrice(int itemID,float newPrice){
        items.get(itemID).updatePrice(newPrice);
    }

    public String getName() {
        return name;
    }

    public void addDynamicOrder(DynamicOrder dynamicOrder) {
        DynamicOrderHistory.add(dynamicOrder);
        this.paymentReceivedFromDeliveries += dynamicOrder.getItemsFromStores().get(id).getDeliveryCost();
        for(ItemInOrder item : dynamicOrder.getItemsFromStores().get(id).getItems()){
            items.get(item.getOrderedItem().getItemId()).increaseNumberOfTimeSold(item.getQuantity());
        }
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public String getStoreOwner() {
        return storeOwner;
    }

    public void addNewFeedback(FeedbackDto newFeedback){
        this.feedbacks.add(new Feedback(newFeedback.getCustomerName(),newFeedback.getOrderId(),newFeedback.getRating(),newFeedback.getFeedback(), newFeedback.getDate()));
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public List<OrderDto> getAllOrders(){
        List<OrderDto> allOrders = new ArrayList<>();
        for(StaticOrder staticOrder : staticOrderHistory){
            allOrders.add(new StaticOrderDto(staticOrder));
        }
        for(DynamicOrder dynamicOrder : DynamicOrderHistory){
            StaticOrderDto staticOrder = new StaticOrderDto(new OrderDetailsDto(dynamicOrder.getItemsFromStores().get(id)),id,
                    dynamicOrder.orderDate,dynamicOrder.customerName,dynamicOrder.getZoneName(),new MapLocationDto(dynamicOrder.getCustomerLocation()));
            staticOrder.setSerialNumber(dynamicOrder.serialNumber);
            allOrders.add(staticOrder);

        }
        return allOrders;
    }

    public float calculatePaymentFromItems() {
        if(DynamicOrderHistory.size() == 0 && staticOrderHistory.size() == 0){
            return 0;
        }

        float result = 0;
        for(StaticOrder order : staticOrderHistory){
            result+= order.getItemsCost();
        }
        for(DynamicOrder order : DynamicOrderHistory){
            result+= order.getOrdersFromStores().get(id).getItemsCost();
        }

        return (float)Math.round(result*100)/100;
    }
}
