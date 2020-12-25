package Engine;

import Dtos.*;
import User.*;

import java.text.DecimalFormat;
import java.util.*;

public interface ZoneInterface {

    static OptionsDto getMenuOptions(Class enumToPrint) {
        OptionsDto menu = new OptionsDto(enumToPrint.getDeclaredFields());
        addSpacesToEnums(menu);
        return menu;
    }

    static void addSpacesToEnums(OptionsDto options) {

        for (int i = 0; i < options.getOptions().length; i++) {
            for (int j = 1; j < options.getOptions()[i].length(); j++) {
                if ((Character.isUpperCase(options.getOptions()[i].charAt(j)))) {
                    options.getOptions()[i] = options.getOptions()[i].substring(0, j) + ' ' + options.getOptions()[i].substring(j, options.getOptions()[i].length());
                    j++;
                }
            }
        }
    }

    static float calculateDistance(MapLocationDto storeLocation , MapLocationDto orderLocation){
        float distance =  (float)(Math.sqrt(Math.pow(storeLocation.getX()-orderLocation.getX() , 2) +
                Math.pow(storeLocation.getY()-orderLocation.getY() , 2)));
        return (float)Math.round(distance*100)/100;
    }
    List<StoreDto> getStoresDetails();
    HashMap<Integer, ItemDto> getAllItems();
    HashMap<Integer, StoreDto> getAllStores();
    float getDeliveryCost(float distance, float PPK);
    StoreDto getStoreById(int storeId);
    boolean isItemExistInMarket(int itemSerialNumber);
    boolean isStoreExistInMarket(int storeId);
    boolean isItemSoldByStore(int itemSerialNumber, StoreDto SelectedStore);
    PurchaseType getItemPurchaseType(int serialNumber);
    void addItemToItemsInOrder(List<ItemInOrderDto> items, StoreDto selectedStore, int serialNumberOfNewItem, float quantity);
    void deleteItemFromStore(int storeID, int itemID);
    void addItemToStore(int storeId, int itemSerialNumber, float price);
    void updateItemPrice(int storeID, int itemID , float price);
    StoreDto findingTheCheapestStore(ItemDto item);
    DynamicOrderDto getDynamicOrderSummary(HashMap<Integer, OrderDetailsDto> itemsToOrder, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName);
    boolean isEmptyLocation(MapLocationDto userLocation);
    void makeDynamicOrder(DynamicOrderDto newOrder, Customer customer, Map<String, User> users, MapLocationDto customerLocation);
    void makeNewStaticOrder(StaticOrderDto newStaticOrder , Customer customer, StoreOwner storeOwner, String zoneName);
    List<DynamicOrderDto> getDynamicOrders();
    List<OrderDto> getAllOrders();
    List<StaticOrderDto> getAllStaticOrders();
    //void restoreStaticOrderFromFile(StaticOrderDto newOrder, int id);
    //void restoreDynamicOrderFromFile(DynamicOrderDto newOrder,int id);
    //HashMap<Integer, CustomerDto> getAllCustomers();
    public StaticOrderDto getStaticOrderSummary(List<ItemFromJsonDto> items, int selectedStoreId, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName);
    List<String> getItemsPrices(int storeId);
    Map<Integer, ItemInStoreDto> getItemsBeingSoldByStore(StoreDto store);
    Map<Integer, ItemDto> getItemsNotBeingSoldByStore(StoreDto store);
    void filterItemsByQuantity(List<ItemInMenuDto> items);
    List<DiscountDto> getPossibleDiscountsToDynamicOrder(HashMap<Integer,List<ItemInOrderDto>> itemsToOrder);
    void getPossibleDiscountsFromOneStore (List<ItemInOrderDto> items, StoreDto store, List<DiscountDto> possibleDiscounts);
    List<ItemInOrderDto> convertToItemInOrder(List<ItemInMenuDto> items);
    HashMap<Integer, List<ItemInOrderDto>> findCheapestStoreForEachItems(List<ItemFromJsonDto> items);
    MapLocationDto getMaxLocationIndexes();
    //CustomerDto getCustomerById(int customerId);
    StoreDto createNewStore(int id, String name, MapLocationDto location, float deliveryPPK, UserDto owner);
    Store addStoreToMarket(StoreDto newStore, List<ItemFromJsonDto> items);
    List<StoreToAddItemDto> getStoresToAddItem();
    void addItemToStores(ItemDto itemToAdd, List<StoreToAddItemDto> stores);
    void addItemToSdm(ItemDto newItem);
    String getZoneName();
    User getZoneOwner();
    int getNumberOfOrders();
    float calculateAverageOrderCost();
    void addNewFeedbackToStore(int storeId,FeedbackDto newFeedback);
    List<OrderDto> getAllStoreOrders(int storeID);
    List<FeedbackDto> getStoreOwnerFeedbacks(String storeOwnerName);
    List<NewStoreAddedAlert> getNewStoreNotifications(String zoneName);
    int getNumberOfItems();
    int getNumberOfStores();
    List<StoreInfoDto> getStoresInfo();
}
