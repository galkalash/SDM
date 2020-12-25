package Engine;

import Dtos.ItemInOrderDto;

public class ItemInOrder {
    private Item orderedItem;
    private float price;
    private float quantity;
    private float totalPrice;
    private boolean isPurchaseWithDiscount;

    public ItemInOrder(Item item, float quantity, float price, boolean isPurchaseWithDiscount){
        orderedItem = item;
        this.quantity = quantity;
        this.price = price;
        totalPrice = this.quantity * this.price;
        this.isPurchaseWithDiscount = isPurchaseWithDiscount;
    }

    public ItemInOrder(ItemInOrderDto itemDto, Item itemReference){
        this.orderedItem = itemReference;
        this.quantity = itemDto.getQuantity();
        this.price = itemDto.getPrice();
        this.totalPrice = this.quantity * this.price;
        this.isPurchaseWithDiscount = itemDto.isPurchaseWithDiscount();
    }

    @Override
    public String toString() {

        StringBuilder orderStr = new StringBuilder();
        orderStr.append("Item Serial Number: " + orderedItem.getItemId() + "\r\n"+
                "Name: " + orderedItem.getName() + "\r\n"+
                "Purchase Type: "+orderedItem.getPurchaseType() +"\r\n"+
                "price: " + price + "\r\n");
        if(orderedItem.getPurchaseType().equals(PurchaseType.Quantity)) {
            orderStr.append("quantity: " + quantity+ "\r\n");
        }
        else{
            orderStr.append("Weight: " + quantity+ "\r\n");
        }

        orderStr.append("totalPrice: " + totalPrice+ "\r\n");

        return orderStr.toString();
    }

    public Item getOrderedItem() {
        return orderedItem;
    }

    public float getPrice() {
        return (float)Math.round(price*100)/100;
    }

    public float getQuantity() {
        return (float)Math.round(quantity*100)/100;
    }

    public float getTotalPrice() {
        return (float)Math.round(totalPrice*100)/100;
    }

    public boolean isPurchaseWithDiscount() {
        return isPurchaseWithDiscount;
    }
}
