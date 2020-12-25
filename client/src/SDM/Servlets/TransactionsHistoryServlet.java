package SDM.Servlets;

import Dtos.AccountTransactionDto;
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

public class TransactionsHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        String username = SessionUtils.getUsername(req);
        Gson gson = new Gson();

        List<AccountTransactionDto> transactions = sdmInterface.getAllTransactions(username);
        resp.getWriter().write(gson.toJson(transactions));
    }
}
