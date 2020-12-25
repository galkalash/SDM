package Engine;

import java.util.*;

public class OrderDetails {

    private List<ItemInOrder> items;
    private float itemsCost;
    private float distance;
    private float deliveryCost;
    private int numberOfItems;
    private int numberOfItemsTypes;

    public OrderDetails(List<ItemInOrder> items, float deliveryCost , float distance ){
        this.items = items;
        this.deliveryCost = deliveryCost;
        this.numberOfItems =0;
        this.numberOfItemsTypes =0;
        Map<Integer, Item> TempMap = new HashMap<>();
        for(ItemInOrder curItem : this.items){
            itemsCost += curItem.getTotalPrice();
            if(curItem.getOrderedItem().getPurchaseType().equals(PurchaseType.Quantity)) {
                numberOfItems += curItem.getQuantity();
            }
            else{
                numberOfItems++;
            }
            if(!TempMap.containsKey(curItem.getOrderedItem().getItemId())){
                this.numberOfItemsTypes++;
                TempMap.put(curItem.getOrderedItem().getItemId() , curItem.getOrderedItem());
                curItem.getOrderedItem().setNumberOfTimesSold(curItem.getOrderedItem().getNumberOfTimesSold() + curItem.getQuantity());
            }
        }
        this.distance = distance;
    }

    public List<ItemInOrder> getItems() {
        return items;
    }

    public float getItemsCost() {
        return (float)Math.round(itemsCost*100)/100;
    }

    public float getDistance() {
        return (float)Math.round(distance*100)/100;
    }

    public float getDeliveryCost() {
        return (float)Math.round(deliveryCost*100)/100;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getNumberOfItemsTypes() {
        return numberOfItemsTypes;
    }
}
