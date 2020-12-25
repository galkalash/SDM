package Engine;


import Dtos.MapLocationDto;
import User.Customer;

import java.util.Date;
import java.util.List;

public class StaticOrder extends Order {
    private final OrderDetails orderDetails;
    private final int storeId;

    public StaticOrder(Date deliveryDate, List<ItemInOrder> items, float deliveryCost , float distance, int storeId, String customerName , String zoneName, MapLocationDto customerLocation) {
        serialNumber = baseSerialNumber++;
        orderDate = deliveryDate;
        this.orderDetails = new OrderDetails(items,deliveryCost,distance);
        this.storeId = storeId;
        this.customerName = customerName;
        this.zoneName = zoneName;
        this.customerLocation = new MapLocation(customerLocation.getX(), customerLocation.getY());
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public int getStoreId() {
        return storeId;
    }

    public float getItemsCost(){
        return (float)Math.round(orderDetails.getItemsCost()*100)/100;
    }
}
