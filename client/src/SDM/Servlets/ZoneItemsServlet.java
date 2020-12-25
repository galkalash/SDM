package SDM.Servlets;

import Dtos.ItemDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ZoneItemsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        String zoneName = req.getParameter("zone");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        HashMap<Integer, ItemDto> items = sdmInterface.getAllZones().get(zoneName).getMarketItems();
        String json = gson.toJson(items);
        resp.getWriter().write(json);
    }
}
