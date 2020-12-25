package Engine;

import Dtos.*;
import User.*;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface SDMInterface {
    void addNewZone(ZoneInterface newZone);
    HashMap<String, ZoneDto> getAllZones();
    void addNewZone(File xmlFile, String zoneOwnerName);
    int getNumberOfOrders(String zoneName);
    float calculateAverageOrderCost(String zoneName);
    void addMoneyToUser(String username, float amount, Date date);
    float getDeliveryCost(String zoneName , int storeId, MapLocationDto customerLocation);
    boolean isValidLocation(int xLocation, int yLocation, String zone);
    List<DiscountDto> getPossibleDiscounts(String zoneName , StoreDto store, List<ItemInOrderDto> items);
    List<DiscountDto> recalculateAvailableDiscount(List<DiscountDto> availableDiscounts , int discountIndex);
    void makeNewDynamicOrder(DynamicOrderDto newOrder, CustomerDto customer, String zoneName);
    void makeStaticOrder(StaticOrderDto newStaticOrder , CustomerDto customer, String zoneName);
    DynamicOrderDto getNewDynamicOrderSummary(HashMap<Integer, OrderDetailsDto> itemsToOrder, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName);
    StoreDto getStoreFromZoneById(int storeId, String zoneName);
    StaticOrderDto getNewStaticOrderSummary(List<ItemFromJsonDto> items, int selectedStoreId, Date date, CustomerDto customer, MapLocationDto customerLocation, String zoneName);
    HashMap<Integer, List<ItemInOrderDto>> findCheapestStoreForDynamicOrder(List<ItemFromJsonDto> items, String zoneName);
    void addFeedbackToStore(String zoneName, int storeId,FeedbackDto newFeedback, String storeOwnerName);
    List<OrderExecutionAlertDto> getNewOrdersNotifications (String userName , int numberOfOrderSeen);
    List<FeedbackAlertDto> getNewFeedbackNotifications (String userName , int numberOfOrderSeen);
    List<AccountTransactionDto> getAllTransactions(String userName);
    List<OrderDto> getOrdersByCustomerAndZone(String zoneName, String customerName);
    OrderDto getOrderById(String zoneName, String customerName, int orderId);
    List<OrderDto> getOrdersByStore(String zoneName, int storeID);
    List<FeedbackDto> getStoreOwnerFeedbacksInZone(String zoneName, String storeOwnerName);
    boolean isStoreIdAvailableInZone(String zoneName, int storeId);
    void addNewStoreToZone(String zoneName, StoreDto newStore, List<ItemFromJsonDto> items);
    List<NewStoreAddedAlert> getNewStoreNotificationsFromZone (String userName);
    List<ZoneInfoDto> getZonesInfo();
    List<StoreInfoDto> getStoresInfo(String zoneName);
}
