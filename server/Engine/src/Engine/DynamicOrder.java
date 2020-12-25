package Engine;

import Dtos.DynamicOrderDto;
import Dtos.ItemInOrderDto;
import Dtos.MapLocationDto;
import Dtos.OrderDetailsDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicOrder extends Order{
    private final HashMap<Integer, OrderDetails> itemsFromStores;
    private int numberOfItemsInOrder;
    private int numberOfItemsTypesInOrder;
    private float itemsCost;
    private float deliveryCost;


    public DynamicOrder() {
        this.itemsFromStores = new HashMap<Integer, OrderDetails>();
    }

    public DynamicOrder(DynamicOrderDto newOrder , Map<Integer, Item> marketItems , String customerName, String zoneName, MapLocationDto customerLocation){
        serialNumber = baseSerialNumber++;
        this.zoneName = zoneName;
        super.customerName = customerName;
        super.customerLocation = new MapLocation(customerLocation.getX(),customerLocation.getY());
        orderDate = newOrder.getOrderDate();
        itemsFromStores = new HashMap<>();
        numberOfItemsInOrder = 0;
        numberOfItemsTypesInOrder = 0;
        itemsCost = 0;
        deliveryCost = 0;
        HashMap<Integer,ItemInOrderDto> tempItemsMap = new HashMap<>();
        for(Map.Entry<Integer, OrderDetailsDto> storeEntry : newOrder.getOrdersFromStores().entrySet()){
            List<ItemInOrder> items = new ArrayList<ItemInOrder>();
            for(ItemInOrderDto item : storeEntry.getValue().getItems()){
                items.add(new ItemInOrder(item , marketItems.get(item.getOrderedItem().getItemId())));
                if(!(tempItemsMap.containsKey(item.getOrderedItem().getItemId()))){
                    tempItemsMap.put(item.getOrderedItem().getItemId(),item);
                    numberOfItemsTypesInOrder++;
                }
            }
            itemsFromStores.put(storeEntry.getKey(), new OrderDetails(items , storeEntry.getValue().getDeliveryCost() , storeEntry.getValue().getDistance()));
            numberOfItemsInOrder += storeEntry.getValue().getNumberOfItems();
            itemsCost += storeEntry.getValue().getItemsCost();
            deliveryCost += storeEntry.getValue().getDeliveryCost();
        }
    }

    public HashMap<Integer, OrderDetails> getItemsFromStores() {
        return itemsFromStores;
    }

    public int getNumberOfItemsInOrder() {
        return numberOfItemsInOrder;
    }

    public int getNumberOfItemsTypesInOrder() {
        return numberOfItemsTypesInOrder;
    }

    public float getItemsCost() {
        return (float)Math.round(itemsCost*100)/100;
    }

    public float getDeliveryCost() {
        return (float)Math.round(deliveryCost*100)/100;
    }

    public HashMap<Integer, OrderDetails> getOrdersFromStores() {
        return itemsFromStores;
    }
}
