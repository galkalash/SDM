package Dtos;

import Engine.OrderDetails;

public class OrderExecutionAlertDto {
    private final int orderId;
    private final String customerName;
    private final int numberOfItemsTypes;
    private final float itemsCost;
    private final float deliveryCost;

    public OrderExecutionAlertDto(int orderId, String customerName, OrderDetailsDto orderDetails) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.numberOfItemsTypes = orderDetails.getNumberOfItemsTypes();
        this.itemsCost = orderDetails.getItemsCost();
        this.deliveryCost = orderDetails.getDeliveryCost();
    }

    @Override
    public String toString() {
        return "New Order received: " +
                "\n\rorderId:" + orderId +
                "\n\rcustomerName: " + customerName +
                "\n\rnumberOfItemsTypes: " + numberOfItemsTypes +
                "\n\ritemsCost: " + itemsCost +
                "\n\rdeliveryCost: " + deliveryCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNumberOfItemsTypes() {
        return numberOfItemsTypes;
    }

    public float getItemsCost() {
        return itemsCost;
    }

    public float getDeliveryCost() {
        return (float)Math.round(deliveryCost*100)/100;
    }
}
