package SDM.Servlets;

import Dtos.*;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DiscountsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        List<ItemInOrderDto> itemsInOrder = new ArrayList<>();
        Gson gson = new Gson();
        String zone = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        String json = req.getParameter("items");
        StoreDto store = sdmInterface.getAllZones().get(zone).getMarketStores().get(storeId);
        ItemFromJsonDto[] items = gson.fromJson(json, ItemFromJsonDto[].class);
        for(ItemFromJsonDto item : items){
            ItemDto itemDto = sdmInterface.getAllZones().get(zone).getMarketItems().get(item.getItemId());
            itemsInOrder.add(new ItemInOrderDto(itemDto,item.getPrice(),item.getQuantity(),false));
        }
        List<DiscountDto> possibleDiscounts = sdmInterface.getPossibleDiscounts(zone, store, itemsInOrder);
        json = gson.toJson(possibleDiscounts);
        resp.getWriter().write(json);
    }
}
