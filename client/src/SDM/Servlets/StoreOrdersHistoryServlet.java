package SDM.Servlets;

import Dtos.OrderDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StoreOrdersHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        Gson gson = new Gson();

        List<OrderDto> orders = sdmInterface.getOrdersByStore(zoneName,storeId);
        resp.getWriter().write(gson.toJson(orders));
    }
}
