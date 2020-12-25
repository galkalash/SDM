package SDM.Servlets;

import Dtos.MapLocationDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeliveryCostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String zone = req.getParameter("zone");
        int xLocation = Integer.parseInt(req.getParameter("X"));
        int yLocation = Integer.parseInt(req.getParameter("Y"));
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        float deliveryCost = sdmInterface.getDeliveryCost(zone,storeId,new MapLocationDto(xLocation,yLocation));
        resp.getWriter().write(String.valueOf(deliveryCost));
    }
}
