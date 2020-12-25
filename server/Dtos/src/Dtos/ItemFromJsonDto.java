package Dtos;

public class ItemFromJsonDto {
    private int itemId;
    private float price;
    private float quantity;
    private boolean purchasedFromDiscount;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public float getPrice() {
        return (float)Math.round(price*100)/100;
    }

    public float getQuantity() {
        return (float)Math.round(quantity*100)/100;
    }

    public boolean isPurchasedFromDiscount() {
        return purchasedFromDiscount;
    }

    public void setPurchasedFromDiscount(boolean purchasedFromDiscount) {
        this.purchasedFromDiscount = purchasedFromDiscount;
    }

    public boolean isPurchaseWithDiscount() {
        return purchasedFromDiscount;
    }
}
