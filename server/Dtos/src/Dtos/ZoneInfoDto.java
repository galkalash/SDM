package Dtos;

public class ZoneInfoDto {

    private final String ownerName;
    private final String zoneName;
    private final int numberOfItemsType;
    private final int numberOfStores;
    private final int numberOfOrders;
    private final float orderAvgCost;

    public ZoneInfoDto(String ownerName, String zoneName, int numberOfItemsType, int numberOfStores, int numberOfOrders, float orderAvgCost) {
        this.ownerName = ownerName;
        this.zoneName = zoneName;
        this.numberOfItemsType = numberOfItemsType;
        this.numberOfStores = numberOfStores;
        this.numberOfOrders = numberOfOrders;
        this.orderAvgCost = orderAvgCost;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getNumberOfItemsType() {
        return numberOfItemsType;
    }

    public int getNumberOfStores() {
        return numberOfStores;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public float getOrderAvgCost() {
        return orderAvgCost;
    }
}
