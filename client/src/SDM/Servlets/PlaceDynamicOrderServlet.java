package SDM.Servlets;

import Dtos.CustomerDto;
import Dtos.DynamicOrderDto;
import Engine.DynamicOrder;
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

public class PlaceDynamicOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Gson gson = new Gson();

        CustomerDto customer =  (CustomerDto)userManager.getUserByName(SessionUtils.getUsername(req));
        String zoneName = req.getParameter("zone");
        String orderJson = req.getParameter("order");
        DynamicOrderDto newOrder = gson.fromJson(orderJson, DynamicOrderDto.class);
        sdmInterface.makeNewDynamicOrder(newOrder,customer,zoneName);
        customer =  (CustomerDto)userManager.getUserByName(SessionUtils.getUsername(req));
        resp.getWriter().write(String.valueOf(customer.getOrders().get(customer.getOrders().size() - 1).getSerialNumber())) ;
    }
}
