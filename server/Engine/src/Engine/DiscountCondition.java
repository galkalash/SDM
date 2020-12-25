package Engine;

public class DiscountCondition {
    private final double quantity;
    private final int itemId;

    public DiscountCondition(double quantity,int itemId){
        this.quantity = quantity;
        this.itemId = itemId;
    }


    public double getQuantity() {
        return (double)Math.round(quantity*100)/100;
    }

    public int getItemId() {
        return itemId;
    }
}
