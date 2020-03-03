package de.laurens.rezeptwebseite.servlets.recipe;

import com.google.gson.Gson;
import de.laurens.rezeptwebseite.mysql.MySQLConnection;
import de.laurens.rezeptwebseite.secure.User;
import org.omg.CORBA.ARG_IN;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "Rezepte", value = "/rezepte")
public class Recipes extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameterMap().containsKey("ratingInput")) {
            int userID = Integer.parseInt(request.getParameter("userID"));
            int rating = Integer.parseInt(request.getParameter("ratingInput"));
            String recipeID = request.getParameter("recipeID");
            MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
            connection.saveRating(userID, rating, recipeID);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameterMap().containsKey("id")) {
            handleRecipeForwarding(request, response);
        } else if (request.getParameterMap().containsKey("search") || request.getParameterMap().containsKey("category")) {
            int page = 1;
            int amount = 4;
            String input = "";

            if (request.getParameterMap().containsKey("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");

            int offset = 0;
            if (page != 1) {
                offset = page * amount - amount;
            }

            Collection results = null;
            int count = 0;
            if (request.getParameterMap().containsKey("search")) {
                input = request.getParameter("search");
                results = connection.searchRecipes(input, amount, offset);
                count = connection.countRecipeSearch(input);
            }
            if (request.getParameterMap().containsKey("category")) {
                input = request.getParameter("category");
                results = connection.searchRecipesByCategory(input, amount, offset);
                count = connection.countRecipeSearchByCategory(input);
            }

            ArrayList<MinRecipe> list = new ArrayList<MinRecipe>(results);

            for (MinRecipe recipe : list) {
                int i = connection.getAverageRating(recipe.getId());
                recipe.setRating(i);
            }
            request.setAttribute("results", list);
            request.setAttribute("count", count);
            request.setAttribute("page", page);
            request.getRequestDispatcher("recipe_overview.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("recipe_overview.jsp").forward(request, response);
        }

    }

    private void handleRecipeForwarding(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        response.setContentType("text/html");

        MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
        User user = null;
        Recipe recipe;
        int rating;
        boolean own_rating = true;

        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            user = (User) httpSession.getAttribute("user");
            if (user != null) {
                request.setAttribute("user", user.getId());
            }
        }

        if (id.equalsIgnoreCase("r")) {
            ArrayList<Recipe> recipes = connection.getRandomRecipes(1);
            recipe = recipes.get(0);
            request.setAttribute("id", recipe.getId());
            request.getRequestDispatcher("templates/random_forwarding.jsp").forward(request, response);
            return;
        } else {
            recipe = connection.getRecipe(id);
        }

        MySQLConnection uConnection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");
        User author = uConnection.getUser(recipe.getAuthor());

        if (user != null) {
            rating = connection.getRating(id, user.getId());
            if (rating == -1) {
                rating = connection.getAverageRating(id);
                own_rating = false;
            }
        } else {
            rating = connection.getAverageRating(id);
            own_rating = false;
        }

        request.setAttribute("id", recipe.getId());
        request.setAttribute("title", recipe.getTitle().toUpperCase());
        request.setAttribute("information_set", recipe.getInformationSet());
        request.setAttribute("ingredients", recipe.getIngredients());
        request.setAttribute("instruction", recipe.getInstruction().replace("%n%", "<br>"));
        request.setAttribute("images", recipe.getImages());
        request.setAttribute("author", author);
        request.setAttribute("rating", rating);
        request.setAttribute("own_rating", own_rating);
        request.setAttribute("category", recipe.getCategory());

        request.getRequestDispatcher("templates/recipe.jsp").forward(request, response);
    }


}
