package Dtos;

import Engine.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static Engine.ZoneInterface.calculateDistance;

@XmlRootElement(name = "DynamicOrders")
public class DynamicOrderDto extends OrderDto{
    @XmlElement(name = "orderDetails")
    private HashMap<Integer, OrderDetailsDto> ordersFromStores;
    @XmlElement(name = "numberOfItemsInOrder")
    private int numberOfItemsInOrder;
    @XmlElement(name = "numberOfItemsTypesInOrder")
    private int numberOfItemsTypesInOrder;
    @XmlElement(name = "itemsCost")
    private float itemsCost;
    @XmlElement(name = "deliveryCost")
    private float deliveryCost;

    public DynamicOrderDto(){
        ordersFromStores = new HashMap<>();
    }

    public DynamicOrderDto(DynamicOrder dynamicOrder){
        initializeFields(dynamicOrder);
        customerName = dynamicOrder.getCustomerName();
        this.zoneName = dynamicOrder.getZoneName();
        this.customerLocation = new MapLocationDto(dynamicOrder.getCustomerLocation());
    }

    private void initializeFields(DynamicOrder dynamicOrder){
        serialNumber = dynamicOrder.getSerialNumber();
        orderDate = dynamicOrder.getOrderDate();
        this.zoneName = dynamicOrder.getZoneName();
        this.customerLocation = new MapLocationDto(dynamicOrder.getCustomerLocation());
        ordersFromStores = new HashMap<>();
        for(Map.Entry<Integer, OrderDetails> itemEntry : dynamicOrder.getItemsFromStores().entrySet()){
            ordersFromStores.put(itemEntry.getKey() , new OrderDetailsDto(itemEntry.getValue()));
        }
        numberOfItemsInOrder = dynamicOrder.getNumberOfItemsInOrder();
        numberOfItemsTypesInOrder = dynamicOrder.getNumberOfItemsTypesInOrder();
        itemsCost = dynamicOrder.getItemsCost();
        deliveryCost = dynamicOrder.getDeliveryCost();
    }

    public DynamicOrderDto(Map<Integer, Store> marketStores , HashMap<Integer,OrderDetailsDto> items, Date deliveryDate, String customerName, MapLocationDto customerLocation, String zoneName){
        ordersFromStores = new HashMap<>();
        this.customerLocation = customerLocation;
        this.zoneName = zoneName;
        HashSet<Integer> itemsTypes = new HashSet<>();
        serialNumber = 0;
        numberOfItemsTypesInOrder = 0;
        orderDate = deliveryDate;
        this.customerName = customerName;
        for(Map.Entry<Integer, OrderDetailsDto> storeEntry : items.entrySet()) {
            for(ItemInOrderDto item : storeEntry.getValue().getItems()){
                itemsTypes.add(item.getItemId());
            }
            float distance = calculateDistance(new MapLocationDto(marketStores.get(storeEntry.getKey()).getMapLocation()) ,customerLocation);
            float currentStoreDeliveryCost = marketStores.get(storeEntry.getKey()).getDeliveryPpk() * distance;
            deliveryCost += currentStoreDeliveryCost;
            numberOfItemsInOrder += storeEntry.getValue().getNumberOfItems();
            itemsCost += storeEntry.getValue().getItemsCost();
            ordersFromStores.put(storeEntry.getKey(), new OrderDetailsDto(storeEntry.getValue().getItems(),distance,currentStoreDeliveryCost));
        }
        numberOfItemsTypesInOrder = itemsTypes.size();
        itemsCost = (float)Math.round(itemsCost*100)/100;
        deliveryCost = (float)Math.round(deliveryCost*100)/100;
    }

    public HashMap<Integer, OrderDetailsDto> getOrdersFromStores() {
        return ordersFromStores;
    }

    public int getNumberOfItemsInOrder() {
        return numberOfItemsInOrder;
    }

    public int getNumberOfItemsTypesInOrder() {
        return numberOfItemsTypesInOrder;
    }

    public float getItemsCost() {
        return itemsCost;
    }

    public float getDeliveryCost() {
        return (float)Math.round(deliveryCost*100)/100;
    }
}
