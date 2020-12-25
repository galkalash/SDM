package Dtos;

import Engine.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "itemInfo")
public class ItemDto {
    @XmlElement(name = "itemId")
    private int itemId;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "purchaseType")
    private PurchaseType purchaseType;
    @XmlElement(name = "averagePrice")
    private float averagePrice;
    @XmlElement(name = "numberOfStoresThatSellingThisItem")
    private int numberOfStoresThatSellingThisItem;
    @XmlElement(name = "numberOfTimesSold")
    private float numberOfTimesSold;

    public ItemDto(){

    }

    public ItemDto(Item item) {
        itemId = item.getItemId();
        name = item.getName();
        purchaseType = item.getPurchaseType();
        averagePrice = item.getAveragePrice();
        numberOfStoresThatSellingThisItem = item.getNumberOfStoresThatSellingThisItem();
        numberOfTimesSold = item.getNumberOfTimesSold();
    }

    public ItemDto(int itemId, String name, PurchaseType purchaseType) {
        this.itemId = itemId;
        this.name = name;
        this.purchaseType = purchaseType;
        averagePrice = 0;
        numberOfStoresThatSellingThisItem = 0;
        numberOfTimesSold = 0;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public float getAveragePrice() {
        return (float)Math.round(averagePrice*100)/100;
    }

    public int getNumberOfStoresThatSellingThisItem() {
        return numberOfStoresThatSellingThisItem;
    }

    public float getNumberOfTimesSold() {
        return (float)Math.round(numberOfTimesSold*100)/100;
    }

    @Override
    public String toString() {
        return itemId + " " +name;
    }
}
