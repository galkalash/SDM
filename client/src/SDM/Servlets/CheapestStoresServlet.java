package SDM.Servlets;

import Dtos.ItemFromJsonDto;
import Dtos.ItemInOrderDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CheapestStoresServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();

        String zoneName = req.getParameter("zone");
        String itemsJson = req.getParameter("items");
        ItemFromJsonDto[] selectedItems = gson.fromJson(itemsJson, ItemFromJsonDto[].class);
        HashMap<Integer, List<ItemInOrderDto>> storeToCheapestItems = sdmInterface.findCheapestStoreForDynamicOrder(Arrays.asList(selectedItems), zoneName);
        resp.getWriter().write(gson.toJson(storeToCheapestItems));
    }
}
