package Dtos;

import Engine.*;
import User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneDto {
    private final String zoneName;
    private final UserDto zoneOwner;
    private final HashMap<Integer, ItemDto> marketItems;
    private final HashMap<Integer, StoreDto> marketStores;
    //private final HashMap<Integer, CustomerDto> marketCustomers;
    private final List<DynamicOrderDto> dynamicOrders;

    public ZoneDto(ZoneInterface zone) {
        zoneName=zone.getZoneName();
        zoneOwner = new UserDto(zone.getZoneOwner());
        marketItems = zone.getAllItems();
        marketStores = zone.getAllStores();
        //marketCustomers = zone.getAllCustomers();
        dynamicOrders = zone.getDynamicOrders();
    }

    public HashMap<Integer, ItemDto> getMarketItems() {
        return marketItems;
    }

    public HashMap<Integer, StoreDto> getMarketStores() {
        return marketStores;
    }
}
