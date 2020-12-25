package Dtos;

public class NewStoreAddedAlert {
    private final String storeName;
    private final String ownerName;
    private final MapLocationDto storeLocation;
    private final int numberOfItemsInStore;
    private final int numberOfItemsInZone;
    private final String zoneName;

    public NewStoreAddedAlert(StoreDto store, int numberOfItemsInZone, String zoneName) {
        this.storeName = store.getName();
        this.ownerName = store.getOwner();
        this.storeLocation = store.getLocation();
        this.numberOfItemsInStore = store.getItems().size();
        this.numberOfItemsInZone = numberOfItemsInZone;
        this.zoneName = zoneName;
    }

    @Override
    public String toString() {
        return "New Store Added" +
                "storeName: '" + storeName +
                ", ownerName: '" + ownerName+
                ", storeLocation: " + storeLocation +
                ", number Of Items In Store: " + numberOfItemsInStore + '/' + numberOfItemsInZone;
    }
}
