package SDM.Servlets;

import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMoneyToUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        String amountToAdd = req.getParameter("amount");
        String dateStr = req.getParameter("date");
        if(dateStr.equals("")){
            resp.getWriter().write("ERROR. invalid date");
            return;
        }
        try{
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            float amount = Float.parseFloat(amountToAdd);
            if(amount<0){
                resp.getWriter().write("ERROR. value should be a positive number.");
                return;
            }

            sdmInterface.addMoneyToUser(username,amount,date);
            resp.getWriter().write("Money added successfully");
        }
        catch (NumberFormatException exp){
            resp.getWriter().write("ERROR. value should be a positive number.");
        } catch (ParseException ignored) {
        }
    }
}
