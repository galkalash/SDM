package Dtos;

import Engine.MapLocation;

public class StoreInfoDto {

    final private int id;
    final private String name;
    final private String ownerName;
    final private int numberOfOrders;
    final private float totalPaymentForItems;
    final private float deliveryPricePerKilometer;
    final private float totalPaymentForDeliveries;
    final private MapLocationDto location;

    public StoreInfoDto(int id, String name, String ownerName, int numberOfOrders,
                        float totalPaymentForItems, float deliveryPricePerKilometer, float totalPaymentForDeliveries, MapLocation location) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.numberOfOrders = numberOfOrders;
        this.totalPaymentForItems = totalPaymentForItems;
        this.deliveryPricePerKilometer = deliveryPricePerKilometer;
        this.totalPaymentForDeliveries = totalPaymentForDeliveries;
        this.location =  new MapLocationDto(location);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public float getTotalPaymentForItems() {
        return totalPaymentForItems;
    }

    public float getDeliveryPricePerKilometer() {
        return deliveryPricePerKilometer;
    }

    public float getTotalPaymentForDeliveries() {
        return totalPaymentForDeliveries;
    }
}
