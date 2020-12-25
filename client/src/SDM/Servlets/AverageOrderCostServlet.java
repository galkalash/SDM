package SDM.Servlets;

import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AverageOrderCostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String zoneName = req.getParameter("zone");
        Gson gson = new Gson();
        SDMInterface sdmManager = ServletUtils.getSDMManager(getServletContext());
        float averageCost = sdmManager.calculateAverageOrderCost(zoneName);
        String json = gson.toJson(averageCost);
        resp.getWriter().write(json);
    }
}
