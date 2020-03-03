package de.laurens.rezeptwebseite.servlets.login;

import com.google.gson.Gson;
import de.laurens.rezeptwebseite.mysql.MySQLConnection;
import de.laurens.rezeptwebseite.secure.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "SignUp", value = "/signup")
public class SignUp extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Map<String, Object> map = new HashMap<>();

        System.out.println("executed");


        MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");
        int result = connection.createUser(username, email, password);

        if (result == 0) {
            System.err.println("An error occurred while attempting to create");
            map.put("status", "error");
            map.put("success", false);
        }
        if (result == 3) {
            System.err.println("The username is already taken");
            map.put("status", "this username is already taken");
            map.put("success", false);
        }
        if (result == 2) {
            System.err.println("The email adress is already taken");
            map.put("status", "the email adess is already taken");
            map.put("success", false);
        }
        if (result == 1) {
            //sign up successfully
            //logging in
            User user = connection.getUser(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(0);
            }
            map.put("success", true);
        }
        write(response, map);
    }

    private void write(HttpServletResponse response, Map<String, Object> map) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(map));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login/register.jsp").forward(request, response);
    }
}
