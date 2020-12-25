package Dtos;

import Engine.DiscountOffer;

public class DiscountOfferDto {
    protected double quantity;
    protected int itemId;
    protected int forAdditional;
    protected String name;

    public DiscountOfferDto(double quantity, int itemId, int forAdditional, String name) {
        this.quantity = quantity;
        this.itemId = itemId;
        this.forAdditional = forAdditional;
        this.name = name;
    }

    public DiscountOfferDto(DiscountOffer discountOffer) {
        quantity = discountOffer.getQuantity();
        itemId = discountOffer.getItemId();
        forAdditional = discountOffer.getForAdditional();
        this.name = discountOffer.getName();
    }

    public double getQuantity() {
        return (double)Math.round(quantity*100)/100;
    }

    public int getItemId() {
        return itemId;
    }

    public int getForAdditional() {
        return forAdditional;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setForAdditional(int forAdditional) {
        this.forAdditional = forAdditional;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return quantity + " of " + name + " for " + forAdditional + " each ";
    }

    public String getName() {
        return name;
    }
}
