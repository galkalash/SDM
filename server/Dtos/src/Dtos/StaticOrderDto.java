package Dtos;

import Engine.ItemInOrder;
import Engine.OrderDetails;
import Engine.StaticOrder;
import Engine.Store;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "StaticOrdersOrders")
public class StaticOrderDto extends OrderDto {
    @XmlElement(name = "orderDetails")
    private OrderDetailsDto orderDetails;
    @XmlElement(name = "storeId")
    private int storeId;

    public StaticOrderDto(){
        orderDetails = new OrderDetailsDto();
    }

    public StaticOrderDto (StaticOrder staticOrder){
        initializeFields(staticOrder);
        customerName = staticOrder.getCustomerName();
        zoneName = staticOrder.getZoneName();
        customerLocation = new MapLocationDto(staticOrder.getCustomerLocation());
    }

    private void initializeFields(StaticOrder staticOrder){
        serialNumber = staticOrder.getSerialNumber();
        orderDate = staticOrder.getOrderDate();
        orderDetails = new OrderDetailsDto(staticOrder.getOrderDetails());
        storeId = staticOrder.getStoreId();
        zoneName = staticOrder.getZoneName();
        customerLocation = new MapLocationDto(staticOrder.getCustomerLocation());
    }

    public StaticOrderDto(OrderDetailsDto orderDetails, int storeId, Date deliveryDate, String customerName, String zoneName, MapLocationDto customerLocation){
        this.orderDetails = orderDetails;
        this.storeId = storeId;
        this.orderDate = deliveryDate;
        this.customerName = customerName;
        this.zoneName = zoneName;
        this.customerLocation = customerLocation;
    }


    public OrderDetailsDto getOrderDetails() {
        return orderDetails;
    }

    public int getStoreId() {
        return storeId;
    }


    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getNumberOfItems(){
        return orderDetails.getNumberOfItems();
    }

    public float getItemsCost(){
        return (float)Math.round(orderDetails.getItemsCost()*100)/100;
    }

    public float getDeliveryCost(){
        return (float)Math.round(orderDetails.getDeliveryCost()*100)/100;
    }

    public float getTotalOrderCost(){
        return (float)Math.round((orderDetails.getItemsCost() + orderDetails.getDeliveryCost())*100)/100;
    }
}
