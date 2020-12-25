package SDM.Servlets;

import Dtos.StoreDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StoreDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();

        int storeId = Integer.parseInt(req.getParameter("storeId"));
        String zoneName = req.getParameter("zone");
        StoreDto store = sdmInterface.getStoreFromZoneById(storeId,zoneName);
        resp.getWriter().write(gson.toJson(store));
    }
}
