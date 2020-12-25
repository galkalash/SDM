package Engine;

public class ItemInStore {
    private int id;
    private String name;
    private float price;
    private PurchaseType purchaseType;
    private float numberOfTimeSold;

    public ItemInStore(float price , Item item){
        this.id = item.getItemId();
        this.name = item.getName();
        this.purchaseType = item.getPurchaseType();
        this.price = price;
        numberOfTimeSold = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getNumberOfTimeSold() {
        return (float)Math.round(numberOfTimeSold*100)/100;
    }

    public float getPrice() {
        return (float)Math.round(price*100)/100;
    }

    public void increaseNumberOfTimeSold(float numberToAdd){
        numberOfTimeSold+=numberToAdd;
    }

    public void updatePrice(float newPrice){
        price = newPrice;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }
}
