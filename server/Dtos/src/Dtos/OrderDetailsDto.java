package Dtos;

import Engine.ItemInOrder;
import Engine.OrderDetails;
import Engine.PurchaseType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "orderDetails")
public class OrderDetailsDto {
    @XmlElement(name = "orderedItems")
    private List<ItemInOrderDto> items;
    @XmlElement(name = "itemsCost")
    private float itemsCost;
    @XmlElement(name = "distance")
    private float distance;
    @XmlElement(name = "deliveryCost")
    private float deliveryCost;
    @XmlElement(name = "numberOfItems")
    private int numberOfItems;
    @XmlElement(name = "numberOfItemsTypes")
    private int numberOfItemsTypes;


    public OrderDetailsDto(){
        items = new ArrayList<>();

    }

    public OrderDetailsDto(OrderDetails orderDetails){
        items = new ArrayList<ItemInOrderDto>();
        for(ItemInOrder item: orderDetails.getItems()){
            items.add(new ItemInOrderDto(item));
        }
        this.itemsCost = orderDetails.getItemsCost();
        this.distance = orderDetails.getDistance();
        this.deliveryCost = orderDetails.getDeliveryCost();
        this.numberOfItems = orderDetails.getNumberOfItems();
        this.numberOfItemsTypes = orderDetails.getNumberOfItemsTypes();
    }

    public OrderDetailsDto(List<ItemInOrderDto> items, float distance, float deliveryCost) {
        itemsCost = 0;
        numberOfItems= 0;
        numberOfItemsTypes = 0;
        this.distance = distance;
        this.deliveryCost = deliveryCost;
        this.items = new ArrayList<ItemInOrderDto>();

        for (ItemInOrderDto item : items) {
            boolean isExist = false;
            for(ItemInOrderDto newItem : this.items) {
                if(newItem.getItemId() == item.getItemId()){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                numberOfItemsTypes++;
            }
            this.items.add(item);
            itemsCost += item.getTotalPrice();
            if(item.getOrderedItem().getPurchaseType() == PurchaseType.Quantity){
                numberOfItems += item.getQuantity();
            }
            else{
                numberOfItems++;
            }
        }
    }

    public List<ItemInOrderDto> getItems() {
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

    public void setItems(List<ItemInOrderDto> items) {
        this.items = items;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public float getTotalOrderCost(){
        return (float)Math.round((deliveryCost + itemsCost)*100)/100;
    }



}
