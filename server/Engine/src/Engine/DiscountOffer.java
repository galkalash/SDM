package Engine;


public class DiscountOffer {

    protected final double quantity;
    protected final int itemId;
    protected final int forAdditional;
    protected final String name;

    public DiscountOffer(double quantity, int itemId, int forAdditional, String name){
        this.quantity = quantity;
        this.itemId = itemId;
        this.forAdditional = forAdditional;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
