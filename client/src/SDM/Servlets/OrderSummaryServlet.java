package SDM.Servlets;

import Dtos.*;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.User;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OrderSummaryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Gson gson = new Gson();

        String username = SessionUtils.getUsername(req);
        UserDto customer = userManager.getUserByName(username);
        String itemsJson = req.getParameter("items");
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        int xLocation = Integer.parseInt(req.getParameter("xLocation"));
        int yLocation = Integer.parseInt(req.getParameter("yLocation"));
        String deliveryDate = req.getParameter("deliveryDate");

        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(deliveryDate);
            ItemFromJsonDto[] items = gson.fromJson(itemsJson,ItemFromJsonDto[].class);
            StaticOrderDto order = sdmInterface.getNewStaticOrderSummary(Arrays.asList(items),storeId, date, (CustomerDto) customer,new MapLocationDto(xLocation,yLocation),zoneName);
            resp.getWriter().write(gson.toJson(order));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
