package SDM.Servlets;

import Dtos.OrderExecutionAlertDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderNotificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();

        String storeOwnerName = SessionUtils.getUsername(req);
        int numberOfOrdersSeen = Integer.parseInt(req.getParameter("ordersSeen"));
        List<OrderExecutionAlertDto> newOrders = sdmInterface.getNewOrdersNotifications(storeOwnerName,numberOfOrdersSeen);
        resp.getWriter().write(gson.toJson(newOrders));
    }
}
