package SDM.Servlets;

import Dtos.FeedbackDto;
import Engine.Rating;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFeedbackServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String customerName = SessionUtils.getUsername(req);
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String feedbackText = req.getParameter("feedback");
        String dateStr = req.getParameter("date");
        String storeOwnerName = sdmInterface.getAllZones().get(zoneName).getMarketStores().get(storeId).getOwner();
        Rating currentRating = null;
        switch (rating){
            case 1:{
                currentRating = Rating.One;
                break;
            }
            case 2:{
                currentRating = Rating.Two;
                break;
            }
            case 3:{
                currentRating = Rating.Three;
                break;
            }
            case 4:{
                currentRating = Rating.Four;
                break;
            }
            case 5:{
                currentRating = Rating.Five;
                break;
            }
        }
        try{
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            FeedbackDto newFeedback = new FeedbackDto(customerName,orderId,currentRating,feedbackText,date);
            sdmInterface.addFeedbackToStore(zoneName,storeId,newFeedback, storeOwnerName);
        }
        catch (Exception ignored){}
    }
}
