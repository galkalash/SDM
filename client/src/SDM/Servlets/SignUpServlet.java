package SDM.Servlets;

import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.UserManager;
import User.UserType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter -
                //redirect back to the index page
                //this return an HTTP code back to the browser telling it to load
                response.setHeader("redirect", "../mainPage/mainPage.html");
                //response.sendRedirect("../signUp/signUp.html");
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                String userTypeStr = request.getParameter("userType");
                UserType userType;
                if(userTypeStr.equals("customer")){
                    userType = UserType.customer;
                }
                else{
                    userType = UserType.shopOwner;
                }

                synchronized (this) {
                    if (userManager.isUserExist(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        response.setHeader("error", "ERROR. username " + usernameFromParameter + " already in use.");
                    } else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter, userType);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute("username", usernameFromParameter);

                        response.setHeader("redirect", "../mainPage/mainPage.html");
                    }
                }
            }
        } else {
            //user is already logged in
            response.setHeader("redirect", "../mainPage/mainPage.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
