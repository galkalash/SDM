package Dtos;

import Engine.ItemInStore;
import Engine.PurchaseType;

public class ItemInStoreDto {
    private int id;
    private String name;
    private float price;
    private PurchaseType purchaseType;
    private float numberOfTimeSold;

    public ItemInStoreDto(ItemInStore itemInStore){
        this.id = itemInStore.getId();
        this.name = itemInStore.getName();
        price = itemInStore.getPrice();
        this.purchaseType = itemInStore.getPurchaseType();
        numberOfTimeSold = itemInStore.getNumberOfTimeSold();
    }

    public float getPrice() {
        return (float)Math.round(price*100)/100;
    }

    public float getNumberOfTimeSold() {
        return numberOfTimeSold;
    }

    @Override
    public String toString() {
        return id +" "+ name +" "+ price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }
}
