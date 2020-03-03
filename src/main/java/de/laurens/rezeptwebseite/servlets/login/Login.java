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

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        boolean isValid = false;

        MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");

        //logging in
        User user = connection.getUser(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(0);

            //Sending user to index
            //response.sendRedirect("index.jsp");
            isValid = true;
            map.put("isValid", isValid);
            write(response, map);

        } else {
            //request.setAttribute("status", "Invalid email or password!");
            //request.getRequestDispatcher("login/login.jsp").include(request, response);
            map.put("isValid", isValid);
            write(response, map);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login/login.jsp").forward(request, response);
    }

    private void write(HttpServletResponse response, Map<String, Object> map) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(map));
    }
}
