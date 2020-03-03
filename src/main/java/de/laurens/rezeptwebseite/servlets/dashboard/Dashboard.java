package de.laurens.rezeptwebseite.servlets.dashboard;

import de.laurens.rezeptwebseite.mysql.MySQLConnection;
import de.laurens.rezeptwebseite.secure.User;
import de.laurens.rezeptwebseite.servlets.recipe.Recipe;
import de.laurens.rezeptwebseite.utils.RecipeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Dashboard", value = "/dashboard")
public class Dashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession httpSession = request.getSession(false);
        if (request.getParameterMap().containsKey("cr-title")) {
            //getting parameters
            String title = request.getParameter("cr-title");
            String ingredients = request.getParameter("cr-ingredients");
            String instruction = request.getParameter("cr-instruction");


            //TODO fix the images
            String[] images = {"-1"};
            String category = request.getParameter("cr-category");
            String keywords = request.getParameter("cr-keywords");
            String informationSet = request.getParameter("cr-information-set");

            User author = (User) httpSession.getAttribute("user");
            if (request.getParameterMap().containsKey("cr-id")) {
                String id = request.getParameter("cr-id");
                handleEditRecipe(id, title, informationSet, ingredients, instruction, images, category, keywords, author, request, response);
            } else {
                handleCreateRecipe(title, informationSet, ingredients, instruction, images, category, keywords, author, request, response);
            }
        }

        if (request.getParameterMap().containsKey("ch-username")) {
            //Change account informations
            String username = request.getParameter("ch-username");
            String oldPassword = request.getParameter("ch-password-old");
            String newPassword = request.getParameter("ch-password-new");

            if ((!oldPassword.isEmpty()) && (!newPassword.isEmpty())) {

                User user = (User) httpSession.getAttribute("user");
                if (user != null) {
                    MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");
                    int result = connection.changePassword(user.getId(), oldPassword, newPassword);
                    response.sendRedirect("/dashboard");
                }
            }

            //check if new username is identical with the old one
            User user = (User) httpSession.getAttribute("user");
            if (!user.getUsername().equals(username)) {
                //change username

                MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");
                int result = connection.changeUserName(username, user.getId());
                if (result == 1) {
                    //username successfully changed
                    System.out.println(">" + user.getUsername() + "' name was changed to " + username);

                    //kill and create a new session to update the username in the user-session instance
                    request.getSession().invalidate();
                    HttpSession session = request.getSession();
                    session.setAttribute("user", connection.getUser(user.getId()));
                    response.sendRedirect("/dashboard");
                }
                if (result == 0) {
                    //error
                    System.err.println(">failed to change " + user.getUsername() + "' name to " + username);
                }
            }
        }

    }


    private void handleCreateRecipe(String title, String informationSet, String ingredients, String instruction, String[] images, String category, String keywords, User author, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = RecipeUtils.createRandomRecipeID(5);
        MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
        int result = connection.createRecipe(id, title, informationSet, ingredients, instruction, images, category, keywords, author.getId());

        if (result == 3) {
            //the id is alread taken
            //creating a new one
            handleCreateRecipe(title, informationSet, ingredients, instruction, images, category, keywords, author, request, response);
        }

        if (result == 2) {
            //the title is already taken
            response.sendRedirect("createrecipe.jsp");
        }

        if (result == 1) {
            //The recipe was successfully saved
            response.sendRedirect("/rezepte?id=" + id);
        }

        if (result == 0) {
            System.err.println("An error occurred while attempting to create");
        }
    }

    private void handleEditRecipe(String id, String title, String informationSet, String ingredients, String instruction, String[] images, String category, String keywords, User author, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
        int result = connection.editRecipe(id, title, informationSet, ingredients, instruction, images, category, keywords, author.getId());

        if (result == 2) {
            //TODO fix
            response.sendRedirect("/rezepte?id=" + id);
        }

        if (result == 1) {
            //The recipe was successfully saved
            response.sendRedirect("/rezepte?id=" + id);
        }

        if (result == 0) {
            System.err.println("An error occurred while attempting to edit");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String page = request.getParameter("p");
        String recipeId = request.getParameter("rid");

        HttpSession httpSession = request.getSession(false);
        MySQLConnection uConnection = new MySQLConnection("127.0.0.1", "3306", "userdata", "root", "laurens");

        if (httpSession != null) {
            User user = (User) httpSession.getAttribute("user");

            if (user != null) {
                //Replace ${Username} to username
                request.setAttribute("email", user.getEmail());
                request.setAttribute("username", user.getUsername());


                if (page != null) {
                    if (page.equalsIgnoreCase("myaccount")) {
                        request.getRequestDispatcher("dashboard/myaccount.jsp").forward(request, response);
                    }
                    if (page.equalsIgnoreCase("myrecipes")) {
                        request.setAttribute("userid", user.getId());

                        request.getRequestDispatcher("dashboard/myrecipes.jsp").forward(request, response);
                    }
                    if (page.equalsIgnoreCase("createrecipe")) {
                        request.getRequestDispatcher("dashboard/createrecipe.jsp").forward(request, response);
                    }
                    if (page.equalsIgnoreCase("editrecipe")) {
                        if (recipeId != null) {
                            //fetch recipe
                            MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
                            Recipe recipe = connection.getRecipe(recipeId);

                            //fetch author
                            User author = uConnection.getUser(recipe.getAuthor());

                            //set attributes
                            request.setAttribute("id", recipe.getId());
                            request.setAttribute("title", recipe.getTitle());
                            request.setAttribute("information_set", recipe.getInformationSet());
                            request.setAttribute("ingredients", recipe.getIngredients());
                            request.setAttribute("instruction", recipe.getInstruction().replace("%n%", "&#13;&#10;"));
                            request.setAttribute("images", recipe.getImages());
                            request.setAttribute("keywords", recipe.getKeywords());
                            request.setAttribute("author", author);
                            //forward user
                            request.getRequestDispatcher("dashboard/editrecipe.jsp").forward(request, response);
                        } else {
                            request.getRequestDispatcher("dashboard/myaccount.jsp").forward(request, response);
                        }
                    }
                    if (page.equalsIgnoreCase("delrecipe")) {
                        if (recipeId != null) {
                            MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
                            connection.deleteRecipe(recipeId);
                            response.sendRedirect("/dashboard?p=myrecipes");

                        }
                    }
                } else {
                    response.sendRedirect("/dashboard?p=myaccount");
                }
            } else {
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/");
        }
    }
}
