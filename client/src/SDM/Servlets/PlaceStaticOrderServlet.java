package SDM.Servlets;

import Dtos.CustomerDto;
import Dtos.StaticOrderDto;
import Dtos.UserDto;
import Engine.SDMInterface;
import Engine.StaticOrder;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.Customer;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlaceStaticOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        Gson gson = new Gson();

        CustomerDto customer = (CustomerDto)userManager.getUserByName(username);
        String zoneName = req.getParameter("zone");
        String orderJson = req.getParameter("order");
        StaticOrderDto order = gson.fromJson(orderJson, StaticOrderDto.class);
        sdmInterface.makeStaticOrder(order,customer,zoneName);
        customer = (CustomerDto)userManager.getUserByName(username);
        resp.getWriter().write(String.valueOf(customer.getOrders().get(customer.getOrders().size() - 1).getSerialNumber()));
    }
}
