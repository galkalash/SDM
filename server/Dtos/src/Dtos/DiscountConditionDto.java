package Dtos;

import Engine.DiscountCondition;

public class DiscountConditionDto {

    private double quantity;
    private int itemId;
    private String itemName;

    public DiscountConditionDto(double quantity, int itemId , String itemName) {
        this.quantity = quantity;
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public DiscountConditionDto(DiscountCondition discountCondition, String itemName) {
        quantity = discountCondition.getQuantity();
        itemId = discountCondition.getItemId();
        this.itemName = itemName;
    }

    public double getQuantity() {
        return (double)Math.round(quantity*100)/100;
    }

    public int getItemId() {
        return itemId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }
}
