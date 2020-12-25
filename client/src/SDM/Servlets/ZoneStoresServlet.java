package SDM.Servlets;

import Dtos.StoreDto;
import Dtos.StoreInfoDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ZoneStoresServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String zoneName = req.getParameter("zone");
        Gson gson = new Gson();
        //HashMap<Integer, StoreDto> stores = sdmInterface.getAllZones().get(zoneName).getMarketStores();
        List<StoreInfoDto> stores = sdmInterface.getStoresInfo(zoneName);
        String json = gson.toJson(stores);
        resp.getWriter().write(json);
    }
}
