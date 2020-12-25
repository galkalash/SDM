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

public class PaymentFromItemsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmManager = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        StoreDto store = sdmManager.getAllZones().get(zoneName).getMarketStores().get(storeId);
        String json = gson.toJson(store.calculatePaymentFromItems());
        resp.getWriter().write(json);
    }
}
