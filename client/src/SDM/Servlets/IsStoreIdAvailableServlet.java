package SDM.Servlets;

import Engine.SDMInterface;
import SDM.Utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IsStoreIdAvailableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));

        resp.getWriter().write(String.valueOf(sdmInterface.isStoreIdAvailableInZone(zoneName,storeId)));
    }
}
