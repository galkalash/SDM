package Dtos;

import Engine.ItemInOrder;
import Engine.PurchaseType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderedItems")
public class ItemInOrderDto {
    @XmlElement(name = "itemInfo")
    private ItemDto orderedItem;
    @XmlElement(name = "price")
    private float price;
    @XmlElement(name = "quantity")
    private float quantity;
    @XmlElement(name = "totalPrice")
    private float totalPrice;
    private boolean isPurchaseWithDiscount;

    public ItemInOrderDto(){

    }

    public ItemInOrderDto(ItemInOrder itemInOrder){
        this.orderedItem = new ItemDto(itemInOrder.getOrderedItem());
        this.price = itemInOrder.getPrice();
        this.quantity = itemInOrder.getQuantity();
        this.totalPrice = itemInOrder.getTotalPrice();
        this.isPurchaseWithDiscount = itemInOrder.isPurchaseWithDiscount();
    }

    public ItemInOrderDto(ItemInMenuDto itemInMenu , ItemDto ItemDto){
        this.orderedItem = ItemDto;
        this.price = Float.parseFloat(itemInMenu.getPrice());
        this.quantity = Float.parseFloat(itemInMenu.getQuantityTextField().getText());
        this.totalPrice = (float)Math.round(price * quantity *100)/100;
    }

    public ItemInOrderDto(ItemDto item, float price, float quantity,boolean isPurchaseWithDiscount){
        orderedItem = item;
        this.price = price;
        this.quantity = quantity;
        totalPrice =  (float)Math.round(price * quantity*100)/100;
        this.isPurchaseWithDiscount = isPurchaseWithDiscount;
    }

    public ItemDto getOrderedItem() {
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

    public void setPurchaseWithDiscount(boolean purchaseWithDiscount) {
        isPurchaseWithDiscount = purchaseWithDiscount;
    }

    public int getItemId(){
        return orderedItem.getItemId();
    }

    public String getItemName(){
        return orderedItem.getName();
    }

    public PurchaseType getItemPurchaseType(){
        return orderedItem.getPurchaseType();
    }

    public String isPurchaseWithDiscountStr(){
        if(isPurchaseWithDiscount){
            return "√";
        }
        return "X";
    }
}