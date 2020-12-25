package SDM.Servlets;

import Dtos.ZoneDto;
import Dtos.ZoneInfoDto;
import SDM.Utils.ServletUtils;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ZonesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        //HashMap<String, ZoneDto> zones = ServletUtils.getSDMManager(getServletContext()).getAllZones();
        List<ZoneInfoDto> zones = ServletUtils.getSDMManager(getServletContext()).getZonesInfo();
        Gson gson = new Gson();
        String json = gson.toJson(zones);
        resp.getWriter().println(json);
        resp.getWriter().flush();
    }
}
