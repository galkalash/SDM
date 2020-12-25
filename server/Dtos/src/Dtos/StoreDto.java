package Dtos;

import Engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreDto {

    private final int id;
    private final String name;
    private final float deliveryPpk;
    private final float paymentReceivedFromDeliveries;
    private final String owner;
    private final Map<Integer, ItemInStoreDto> items;
    private final List<StaticOrderDto> staticOrderHistory;
    private final List<DynamicOrderDto> dynamicOrderHistory;
    private final MapLocationDto location;
    private final List<DiscountDto> discounts;
    private final List<FeedbackDto> feedbacks;

    public StoreDto(Store store) {
        this.id = store.getId();
        this.owner = store.getStoreOwner();
        this.name = store.getName();
        this.deliveryPpk = store.getDeliveryPpk();
        this.paymentReceivedFromDeliveries = store.getPaymentReceivedFromDeliveries();
        this.items = new HashMap<>();
        this.location = new MapLocationDto(store.getMapLocation());
        staticOrderHistory = new ArrayList<StaticOrderDto>();
        dynamicOrderHistory = new ArrayList<DynamicOrderDto>();
        feedbacks = new ArrayList<>();
        discounts = new ArrayList<>();
        for(Map.Entry<Integer, ItemInStore> item: store.getItems().entrySet()){
            items.put(item.getKey(),new ItemInStoreDto(item.getValue()));
        }
        for(StaticOrder order : store.getStaticOrderHistory()){
            this.staticOrderHistory.add(new StaticOrderDto(order));
        }
        for(DynamicOrder order : store.getDynamicOrderHistory()){
            this.dynamicOrderHistory.add(new DynamicOrderDto(order));
        }
        for(Discount discount: store.getDiscounts()){
            this.discounts.add(new DiscountDto(discount, items.get(discount.getCondition().getItemId()).getName()));
        }
        for(Feedback feedback : store.getFeedbacks()){
            feedbacks.add(new FeedbackDto(feedback));
        }
    }

    public StoreDto(int id, String name, float deliveryPpk, MapLocationDto location, UserDto owner) {
        this.id = id;
        this.owner = owner.getName();
        this.name = name;
        this.deliveryPpk = deliveryPpk;
        this.location = location;
        paymentReceivedFromDeliveries = 0;
        items = null;
        dynamicOrderHistory = null;
        discounts = null;
        staticOrderHistory = null;
        feedbacks = null;
    }

    public List<OrderDto> getOrdersDetails(){
        List<OrderDto> orders = new ArrayList<>();
        orders.addAll(staticOrderHistory);
        orders.addAll(dynamicOrderHistory);
        return orders;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getDeliveryPpk() {
        return (float)Math.round(deliveryPpk*100)/100;
    }

    public float getPaymentReceivedFromDeliveries() {
        return paymentReceivedFromDeliveries;
    }

    public Map<Integer, ItemInStoreDto> getItems() {
        return items;
    }

    public MapLocationDto getLocation() {
        return location;
    }

    public List<StaticOrderDto> getStaticOrderHistory() {
        return staticOrderHistory;
    }

    public List<DynamicOrderDto> getDynamicOrderHistory() {
        return dynamicOrderHistory;
    }

    public List<DiscountDto> getDiscounts() {
        return discounts;
    }

    @Override
    public String toString() {
        return id + " " + name + " "+ location.toString();
    }

    public String getOwner() {
        return owner;
    }

    public float calculatePaymentFromItems() {
        if(dynamicOrderHistory.size() == 0 && staticOrderHistory.size() == 0){
            return 0;
        }

        float result = 0;
        for(StaticOrderDto order : staticOrderHistory){
            result+= order.getItemsCost();
        }
        for(DynamicOrderDto order : dynamicOrderHistory){
            result+= order.getOrdersFromStores().get(id).getItemsCost();
        }

        return (float)Math.round(result*100)/100;
    }

    public List<FeedbackDto> getFeedbacks() {
        return feedbacks;
    }
}
