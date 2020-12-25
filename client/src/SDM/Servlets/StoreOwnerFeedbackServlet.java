package SDM.Servlets;

import Dtos.FeedbackDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StoreOwnerFeedbackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        Gson gson = new Gson();

        String storeOwnerName = SessionUtils.getUsername(req);
        String zoneName = req.getParameter("zone");
        List<FeedbackDto> feedbacks = sdmInterface.getStoreOwnerFeedbacksInZone(zoneName,storeOwnerName);
        resp.getWriter().write(gson.toJson(feedbacks));
    }
}
