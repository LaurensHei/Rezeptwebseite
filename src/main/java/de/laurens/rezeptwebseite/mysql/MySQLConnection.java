package de.laurens.rezeptwebseite.mysql;

import de.laurens.rezeptwebseite.secure.AES;
import de.laurens.rezeptwebseite.secure.User;
import de.laurens.rezeptwebseite.servlets.recipe.MinRecipe;
import de.laurens.rezeptwebseite.servlets.recipe.Recipe;

import javax.servlet.ServletException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MySQLConnection {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public MySQLConnection(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * register a new user to the database. After this method, the user is registered and can be logged in
     *
     * @param username set the username of the new user. This name can be seen by everyone.
     * @param email    set the email. The email is only visible for the registered user.
     * @param password set the password. Before the password will be saved in the database, it will be encrypted by
     *                 the AES encrypter with a secret code, there's only in this class.
     * @return returns an integer. This integer shows the status of the register process. 1 = the user is successfully
     * registered. 2 = the email address is already taken. 3 = the username is already taken. 0 = an error occurred.
     */
    public Integer createUser(String username, String email, String password) {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM `users` WHERE username = '" + username + "'");

            if (resultSet.next()) {
                //The username is already taken
                return 3;
            }

            resultSet = statement.executeQuery("SELECT * FROM `users` WHERE email = '" + email + "'");

            if (resultSet.next()) {
                //The email is already taken
                return 2;
            }

            //Save user informations in sql

            //encrypt password
            String encryptedPassword = AES.encrypt(password, "9bWwff5DVLsQR4hgjkmQ");
            statement.executeQuery("SET NAMES 'utf8';");
            statement.executeUpdate("INSERT INTO users (username, email, password) VALUES ('" + username + "', '" + email + "', '" + encryptedPassword + "');");
            return 1;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return 0;
    }

    /**
     * This method change the username from an user. The method is used by the 'Mein Account' page in the dashboard.
     *
     * @param username is the new username.
     * @param id       is the id from the user.
     * @return The method return an Integer which show the status of the process. 1 = The process was successfully.
     * 0 = An error occurred.
     */
    public Integer changeUserName(String username, int id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");

            if (resultSet.next()) {
                int userID = resultSet.getInt("id");
                if (userID != id) {
                    return 2;
                }
            }

            statement.executeUpdate("UPDATE `users` SET `username` = '" + username + "' WHERE users.id = " + id);
            return 1;

            //fixme check if the username is already taken

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return 0;
    }

    public int changePassword(int userID, String oldPassword, String newPassword) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            //encrypt password
            String encryptedPassword = AES.encrypt(oldPassword, "9bWwff5DVLsQR4hgjkmQ");

            resultSet = statement.executeQuery("SELECT * FROM users WHERE password = '" + encryptedPassword + "' AND id = " + userID);

            if (resultSet.next()) {
                String encryptedPasswordNew = AES.encrypt(newPassword, "9bWwff5DVLsQR4hgjkmQ");
                statement.executeUpdate("UPDATE users SET password = '" + encryptedPasswordNew + "' WHERE id = " + userID);
                return 1;
            }

            return 2;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return 0;
    }

    /**
     * This method get an user with email and password. This process need the login page. This method to get the user
     * is the legal way, because it's get only all user informations with the email and password.
     *
     * @param email    the email is in this version to get the user required
     * @param password ..also the password
     * @return after password decrypting the method return a new instance of an user with the user informations from the
     * database
     */
    public User getUser(String email, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `users` WHERE email = '" + email + "'");

            if (resultSet.next()) {
                String rs_id = resultSet.getString("id");
                String rs_username = resultSet.getString("username");
                String rs_email = resultSet.getString("email");
                String rs_password = resultSet.getString("password");
                String rs_permission = resultSet.getString("permission");

                //decrypting password
                final String secretKey = "9bWwff5DVLsQR4hgjkmQ";
                String decryptedPassword = AES.decrypt(rs_password, secretKey);

                if (decryptedPassword.equals(password)) {
                    return new User(Integer.parseInt(rs_id), rs_username, rs_email, decryptedPassword, rs_permission);
                }

                return null;
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return null;
    }

    /**
     * This is the second way to get all user informations. This way is only for the backend because it's need only the
     * user id to get all user informations. This method needs the recipe template page.
     *
     * @param id is required to command the database, which user is mean.
     * @return return a new instance of an user with all user informations.
     */
    public User getUser(int id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM users WHERE id = " + id);
            if (resultSet.next()) {
                String rs_username = resultSet.getString("username");
                String rs_email = resultSet.getString("email");

                return new User(id, rs_username, rs_email);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return null;
    }

    /**
     * Create a new recipe with all recipe informations. This method is used by the createrecipe page on the dashboard.
     *
     * @param id          Every recipe becomes an unique id. This id will automatically generated by the computer.
     * @param title       The title is the name of the recipe. Determined by the user.
     * @param ingredients The ingredients table is an JSON string build from an javascript array.
     * @param instruction ..also determined by the user.
     * @param images      fixme images aren't available yet.
     * @param category
     * @param keywords    The keywords are also an JSON string build from the keywords input.
     * @param author      The author is an integer, the user id. The user is getting from the login session.
     * @return Return an Integer which show the status from the process. 1 = The process was successfully. 2 = The title
     * is already taken. 3 = The id is already taken. 0 = An error occurred.
     */
    public Integer createRecipe(String id, String title, String informationSet, String ingredients, String instruction, String[] images, String category, String keywords, int author) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM `recipes` WHERE title ='" + title + "'");

            if (resultSet.next()) {
                //The title is already taken
                return 2;
            }

            resultSet = statement.executeQuery("SELECT * FROM `recipes` WHERE id='" + id + "'");
            if (resultSet.next()) {
                return 3;
            }

            //formatting string arrays
            StringBuilder fm_images = new StringBuilder();
            for (String str : images) {
                fm_images.append(str).append(";");
            }

            //save recipe in sql
            statement.executeQuery("SET NAMES 'latin1' COLLATE 'latin1_german1_ci'");
            statement.executeUpdate("INSERT INTO recipes (id, title, information_set, ingredients, instruction, images, category, author, keywords) VALUES ('" + id + "', '" + title + "', '" + informationSet + "', '" + ingredients + "', '" + instruction + "', '" + fm_images + "', '" + category + "','" + author + "', '" + keywords + "' );");
            return 1;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return 0;
    }

    /**
     * This method edit an recipe. The process is the same like the create process, except for a few lines of code.
     *
     * @param id          Every recipe becomes an unique id. This id will automatically generated by the computer.
     * @param title       The title is the name of the recipe. Determined by the user.
     * @param ingredients The ingredients table is an JSON string build from an javascript array.
     * @param instruction ..also determined by the user.
     * @param images
     * @param category
     * @param keywords    The keywords are also an JSON string build from the keywords input.
     * @param author      The author is an integer, the user id. The user is getting from the login session.
     * @return Return an Integer which show the status from the process. 1 = the process was successfully. 2 = The id from
     * the existing recipe aren't the recipe that was editing (so that's not the same recipe and can't save)
     * 0 = An error occurred.
     */
    public Integer editRecipe(String id, String title, String informationSet, String ingredients, String instruction, String[] images, String category, String keywords, int author) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM `recipes` WHERE title ='" + title + "'");
            if (resultSet.next()) {
                String rs_id = resultSet.getString("id");
                //if the id from the result set (existing recipe) aren't the recipe that we editing, that's not the same recipe
                //and cannot save
                if (!rs_id.equals(id)) {
                    System.out.println("ERR");
                    return 2;
                }
            }

            //formatting string arrays
            StringBuilder fm_images = new StringBuilder();
            for (String str : images) {
                fm_images.append(str).append(";");
            }

            //save recipe in sql
            statement.executeQuery("SET NAMES 'latin1_german1_ci';");
            String updateCommand = "UPDATE `recipes` SET `title` = '" +
                    title + "', `ingredients` = '" +
                    ingredients + "', `instruction` = '" +
                    instruction + "', `images` = '" +
                    fm_images + "', `category` = '" +
                    category + "', `author` = '" +
                    author + "', `keywords` = '" +
                    keywords + "' WHERE recipes.id = '" +
                    id + "', `information_set` = '" +
                    informationSet + "'";
            statement.executeUpdate(updateCommand.replace("\"", "\\\""));
            return 1;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return 0;
    }

    public Integer deleteRecipe(String id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM recipes WHERE id ='" + id + "'");
            return 1;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return 0;
    }

    /**
     * This method gets an recipe by id. This method is used by the random recipe function and the recipe page.
     *
     * @param id is required for the database.
     * @return if the process is successfully, the method return a recipe instance with all recipe informations. If the
     * process failed, the method return null.
     * @throws ServletException
     */
    public Recipe getRecipe(String id) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `recipes` WHERE id = '" + id + "'");

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                String ingredients = resultSet.getString("ingredients");
                String instruction = resultSet.getString("instruction");
                String[] images = resultSet.getString("images").split(";");
                String[] keywords = resultSet.getString("keywords").split(";");
                int author = resultSet.getInt("author");

                return new Recipe(id, title, informationSet, category, ingredients, instruction, images, keywords, author);
            }

        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBDC Driver not found.", e);
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        } finally {
            closeDatabaseStuff();
        }

        return null;
    }

    /**
     * This method get all author's recipes. This method is used by the 'Meine Rezepte' page on the dashboard.
     *
     * @param id     the database required the user id from the author to filter the recipes.
     * @param amount limit the results to a number.
     * @param offset set the startpoint from the result limit.
     * @return The method returns an array list with the recipes from the resultSet.
     */
    public ArrayList<Recipe> getRecipesByAuthor(int id, int amount, int offset) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM recipes WHERE author = '" + id + "' LIMIT " + amount + " OFFSET " + offset);

            ArrayList<Recipe> results = new ArrayList<>();

            while (resultSet.next()) {
                String rs_id = resultSet.getString("id");
                String rs_title = resultSet.getString("title");
                String rs_informationSet = resultSet.getString("information_set");
                String rs_category = resultSet.getString("category");
                String rs_ingredients = resultSet.getString("ingredients");
                String rs_instruction = resultSet.getString("instruction");
                String[] rs_images = resultSet.getString("images").split(";");
                String[] rs_keywords = resultSet.getString("keywords").split(";");
                int rs_author = resultSet.getInt("author");

                results.add(new Recipe(rs_id, rs_title, rs_informationSet, rs_category, rs_ingredients, rs_instruction, rs_images, rs_keywords, rs_author));
            }
            return results;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return null;
    }

    /**
     * This method gets all last published recipes. The method is used by the index page.
     *
     * @param amount limit the results to a number.
     * @param offset set the startpoint from the result limit.
     * @return The method returns an array list with the recipes from the resultSet.
     */
    public ArrayList<MinRecipe> getLastPublishedRecipes(int amount, int offset) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM recipes ORDER BY timestamp DESC LIMIT " + amount + " OFFSET " + offset);
            ArrayList<MinRecipe> results = new ArrayList<>();

            while (resultSet.next()) {
                String rs_id = resultSet.getString("id");
                String rs_title = resultSet.getString("title");
                String rs_informationSet = resultSet.getString("information_set");
                String rs_category = resultSet.getString("category");
                int rs_author = resultSet.getInt("author");
                int rs_rating = (int) resultSet.getDouble("rating");

                results.add(new MinRecipe(rs_id, rs_title, rs_informationSet, rs_category, rs_author, rs_rating));
            }
            return results;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return null;
    }

    public ArrayList<MinRecipe> getBestRatedRecipes(int amount, int offset) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM recipes ORDER BY rating DESC LIMIT " + amount + " OFFSET " + offset);

            ArrayList<MinRecipe> recipes = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                int author = resultSet.getInt("author");
                int rating = (int) resultSet.getDouble("rating");

                recipes.add(new MinRecipe(id, title, informationSet, category, author, rating));
            }
            return recipes;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return null;
    }

    /**
     * This method has a search function and is used by the seach function in the header.
     *
     * @param input if the input word contains a recipe name or a keyword, the recipe is add to the result collection.
     * @return The method returns a Collection with all founded recipes from the search process.
     */
    public Collection searchRecipes(String input, int amount, int offset) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM recipes WHERE title LIKE \"%" + input + "%\" LIMIT " + amount + " OFFSET " + offset);

            HashMap<String, MinRecipe> recipes = new HashMap<>();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                int author = resultSet.getInt("author");
                int rating = (int) resultSet.getDouble("rating");

                recipes.put(id, new MinRecipe(id, title, informationSet, category, author, rating));
            }

            resultSet = statement.executeQuery("SELECT * FROM recipes WHERE keywords LIKE \"% " + input + "%\" LIMIT " + amount + " OFFSET " + offset);
            //noinspection Duplicates
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                int author = resultSet.getInt("author");
                int rating = (int) resultSet.getDouble("rating");

                recipes.put(id, new MinRecipe(id, title, informationSet, category, author, rating));
            }

            return recipes.values();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return null;
    }

    public int countRecipeSearch(String input) {
        int sum = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM recipes WHERE title LIKE \"%" + input + "%\"");
            if (resultSet.next()) {
                sum = Integer.parseInt(resultSet.getString(1));
            }
            return sum;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return sum;
    }

    public Collection searchRecipesByCategory(String categoryInput, int amount, int offset) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM recipes WHERE category = '" + categoryInput + "' LIMIT " + amount + " OFFSET " + offset);

            HashMap<String, MinRecipe> recipes = new HashMap<>();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                int author = resultSet.getInt("author");
                int rating = (int) resultSet.getDouble("rating");

                recipes.put(id, new MinRecipe(id, title, informationSet, category, author, rating));
            }
            return recipes.values();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return null;
    }

    public int countRecipeSearchByCategory(String input) {
        int sum = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM recipes WHERE category ='" + input + "'");
            if (resultSet.next()) {
                sum = Integer.parseInt(resultSet.getString(1));
            }
            return sum;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return sum;
    }

    /**
     * This method gets some random recipes and it's used by the random recipe function in the header.
     *
     * @param amount set the amount of random recipes.
     * @return The method returns an ArrayList with all random recipes.
     */
    public ArrayList<Recipe> getRandomRecipes(int amount) {
        ArrayList<Recipe> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `recipes` ORDER BY RAND() LIMIT " + amount);

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String informationSet = resultSet.getString("information_set");
                String category = resultSet.getString("category");
                String ingredients = resultSet.getString("ingredients");
                String instruction = resultSet.getString("instruction");
                String[] images = resultSet.getString("images").split(";");
                String[] keywords = resultSet.getString("keywords").split(";");
                int author = resultSet.getInt("author");

                list.add(new Recipe(id, title, informationSet, category, ingredients, instruction, images, keywords, author));
            }
            return list;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return null;
    }

    /**
     * This method returns an ingredient image path. The method is used by the ingredient section in the recipe page.
     *
     * @param ingredient the ingredient name is required for the database.
     * @return if the resultSet is not null, the method returns the ingredient path from the ingredient above.
     * If no path is found, it returns the path of the notfound image.
     */
    public String getIngredientImagePath(String ingredient) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `ingredients_images` WHERE name = '" + ingredient + "'");
            if (resultSet.next()) {
                return resultSet.getString("path");
            }

            resultSet = statement.executeQuery("SELECT * FROM `ingredients_images` WHERE '" + ingredient + "' LIKE CONCAT('%', name, '%')");
            if (resultSet.next()) {

                String seach_properties = resultSet.getString("seach_properties");
                if (seach_properties.equalsIgnoreCase("like")) {
                    return resultSet.getString("path");
                } else if (seach_properties.equalsIgnoreCase("plural")) {
                    if (resultSet.getString("plural").equalsIgnoreCase(ingredient)) {
                        return resultSet.getString("path");
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return "ingredients/notfound.png";
    }

    public void saveRating(int userID, int rating, String recipeID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM rating WHERE recipeID='" + recipeID + "' AND userID='" + userID + "'");
            if (resultSet.next()) {
                statement.executeUpdate("UPDATE rating SET rating = '" + rating + "' WHERE recipeID='" + recipeID + "' AND userID='" + userID + "'");
                double avg = getExactlyAverageRating(recipeID);
                statement.executeUpdate("UPDATE recipes SET rating = " + avg + " WHERE id='" + recipeID + "'");
                return;
            }

            statement.executeUpdate("INSERT INTO rating(userID, rating, recipeID) VALUES(" + userID + ", " + rating + ", '" + recipeID + "')");
            double avg = getExactlyAverageRating(recipeID);
            statement.executeUpdate("UPDATE recipes SET rating = " + avg + " WHERE id='" + recipeID + "'");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }


    }

    public int getRating(String recipeID, int userID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM rating WHERE recipeID='" + recipeID + "' AND userID='" + userID + "'");
            if (resultSet.next()) {
                return resultSet.getInt("rating");
            }

            return -1;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return -1;
    }

    public int getAverageRating(String recipeID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT AVG(rating) as rslt FROM `rating` WHERE recipeID='" + recipeID + "'");
            if (resultSet.next()) {
                return resultSet.getInt("rslt");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }
        return -1;
    }

    public double getExactlyAverageRating(String recipeID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT AVG(rating) as rslt FROM `rating` WHERE recipeID='" + recipeID + "'");
            if (resultSet.next()) {
                return resultSet.getDouble("rslt");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeDatabaseStuff();
        }

        return -1d;
    }


    /**
     * close some database stuff.
     */
    private void closeDatabaseStuff() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }

            if (statement != null) {
                statement.close();
                statement = null;
            }

            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ignored) {
        }
    }
}
