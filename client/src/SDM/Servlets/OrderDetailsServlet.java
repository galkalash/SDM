package SDM.Servlets;

import Dtos.OrderDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        int orderSerialNumber = Integer.parseInt(req.getParameter("serialNumber"));
        String zoneName = req.getParameter("zone");
        Gson gson = new Gson();

        OrderDto order = sdmInterface.getOrderById(zoneName,username,orderSerialNumber);
        resp.getWriter().write(gson.toJson(order));
    }
}
