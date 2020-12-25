package SDM.Servlets;

import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IsValidLocationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();
        String xLocation = req.getParameter("X");
        String yLocation = req.getParameter("Y");
        String zone = req.getParameter("zone");
        int x = Integer.parseInt(xLocation);
        int y = Integer.parseInt(yLocation);
        boolean isValid = sdmInterface.isValidLocation(x,y,zone);
        resp.getWriter().write(gson.toJson(isValid));
    }
}
