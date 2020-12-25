package SDM.Servlets;

import Dtos.*;
import Engine.DynamicOrder;
import Engine.SDMInterface;
import Engine.ZoneInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DynamicOrderSummaryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String customerUsername = SessionUtils.getUsername(req);
        Gson gson = new Gson();

        String zoneName = req.getParameter("zone");
        String itemsFromStoreJson = req.getParameter("itemsFromStores");
        int xLocation = Integer.parseInt(req.getParameter("xLocation"));
        int yLocation = Integer.parseInt(req.getParameter("yLocation"));
        String deliveryDate = req.getParameter("deliveryDate");
        CustomerDto customer = (CustomerDto) userManager.getUserByName(customerUsername);

        try {
            MapLocationDto customerLocation = new MapLocationDto(xLocation,yLocation);
            ZoneDto zone = sdmInterface.getAllZones().get(zoneName);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(deliveryDate);
            Type mapType = new TypeToken<Map<Integer,List<ItemFromJsonDto>>>() {}.getType();
            HashMap<Integer, List<ItemFromJsonDto>> itemsFromStore = gson.fromJson(itemsFromStoreJson, mapType);

            HashMap<Integer, OrderDetailsDto> ordersFromStores = new HashMap<>();
            for(Map.Entry<Integer,List<ItemFromJsonDto>> storeItems : itemsFromStore.entrySet()){
                List<ItemInOrderDto> items = new ArrayList();
                StoreDto currentStore = zone.getMarketStores().get(storeItems.getKey());
                float distance = ZoneInterface.calculateDistance(currentStore.getLocation(),customerLocation);
                float deliveryCost = distance * currentStore.getDeliveryPpk();
                for(ItemFromJsonDto item : storeItems.getValue()){
                    items.add(new ItemInOrderDto(zone.getMarketItems().get(item.getItemId()),item.getPrice(),item.getQuantity(),item.isPurchaseWithDiscount()));
                }

                OrderDetailsDto orderFromStore = new OrderDetailsDto(items,distance,deliveryCost);
                ordersFromStores.put(storeItems.getKey(),orderFromStore);
            }

            DynamicOrderDto newOrder =  sdmInterface.getNewDynamicOrderSummary(ordersFromStores,date,customer,customerLocation,zoneName);
            resp.getWriter().write(gson.toJson(newOrder));
        } catch (ParseException e) {
        }
    }
}
