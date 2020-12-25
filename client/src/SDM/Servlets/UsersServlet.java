package SDM.Servlets;

import Dtos.UserDto;
import SDM.Utils.ServletUtils;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<UserDto> users = userManager.getAllUsers();
        Gson gson = new Gson();
        String usersJson = gson.toJson(users);
        resp.getWriter().println(usersJson);
        resp.getWriter().flush();
    }
}
