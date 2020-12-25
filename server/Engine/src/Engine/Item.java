package Engine;

import Dtos.ItemDto;

import java.text.DecimalFormat;
import java.util.Objects;

public class Item {

    private final int itemId;
    private final String name;
    private final PurchaseType purchaseType;
    private float averagePrice;
    private int numberOfStoresThatSellingThisItem;
    private float numberOfTimesSold;

    public Item(ItemDto itemDto){
        this.itemId = itemDto.getItemId();
        this.name = itemDto.getName();
        this.purchaseType = itemDto.getPurchaseType();
        this.averagePrice = itemDto.getAveragePrice();
        this.numberOfStoresThatSellingThisItem = itemDto.getNumberOfStoresThatSellingThisItem();
        this.numberOfTimesSold = itemDto.getNumberOfTimesSold();
    }

    public Item(int itemId, String name, PurchaseType type){
        this.itemId = itemId;
        this.name = name;
        this.purchaseType = type;
        averagePrice = 0;
        numberOfStoresThatSellingThisItem = 0;
        numberOfTimesSold = 0;
    }

    public void setAveragePrice(float averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setNumberOfStoresThatSellingThisItem(int numberOfStoresThatSellingThisItem) {
        this.numberOfStoresThatSellingThisItem = numberOfStoresThatSellingThisItem;
    }

    public float getAveragePrice() {
        return (float)Math.round(averagePrice*100)/100;
    }

    public int getNumberOfStoresThatSellingThisItem() {
        return numberOfStoresThatSellingThisItem;
    }


    public int getItemId(){
        return itemId;
    }

    public void setNumberOfTimesSold(float numberOfTimesSold) {
        this.numberOfTimesSold = numberOfTimesSold;
    }

    public float getNumberOfTimesSold() {
        return (float)Math.round(numberOfTimesSold*100)/100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    public String getName() {
        return name;
    }

    public String itemDetails() {
        return "Item Serial Number: " + itemId + "\r\n" +
                "Name: " + name + "\r\n" +
                "Purchase Type:" + purchaseType + "\r\n";
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    @Override
    public String toString() {
        return  "Serial Number: " + itemId + "\r\n" +
                "Name: " + name + "\r\n" +
                "Purchase Type: " + purchaseType + "\r\n" +
                "Number Of Stores That Selling This Item: " + numberOfStoresThatSellingThisItem + "\r\n" +
                "Average Price: " + Float.parseFloat(new DecimalFormat("##.##").format(averagePrice)) + "\r\n" +
                "Number Of Times Sold: " + numberOfTimesSold + "\r\n";
    }

    protected void increaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(float priceInTheNewStore){
        numberOfStoresThatSellingThisItem++;
        float newAveragePrice = (averagePrice * (numberOfStoresThatSellingThisItem-1) + priceInTheNewStore) / numberOfStoresThatSellingThisItem;
        averagePrice = newAveragePrice;
    }

    protected void decreaseNumberOfStoresThatSellingThisItemAndCalculateAveragePrice(float priceOfRemovedItem){
        numberOfStoresThatSellingThisItem--;
        averagePrice = (averagePrice * (numberOfStoresThatSellingThisItem+1) - priceOfRemovedItem) / numberOfStoresThatSellingThisItem;
    }

    protected void updateAveragePriceAfterAChangeInPrice(float previousPrice, float newPrice) {
        averagePrice = (averagePrice * numberOfStoresThatSellingThisItem - previousPrice + newPrice) / numberOfStoresThatSellingThisItem;
    }
}
