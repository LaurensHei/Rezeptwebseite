<%@ page import="de.laurens.rezeptwebseite.mysql.MySQLConnection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="de.laurens.rezeptwebseite.servlets.recipe.Recipe" %><%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 17.02.2018
  Time: 19:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <title>Meine Rezepte</title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/dashboard.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<header>
    <%@include file="../templates/header_dashboard.jsp" %>
</header>


<div class="content">
    <h1 class="align-center">Meine Rezepte</h1>

    <table style="width: 50%; min-width: 25%; margin-top: 40px; margin: 0 auto;" id="ingredient-table">
        <%
            MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
            ArrayList<Recipe> recipes = connection.getRecipesByAuthor((Integer) request.getAttribute("userid"), 25, 0);
            for (Recipe recipe : recipes) {
                out.println("<tr>");
                out.println("<td><a href=\"/rezepte?id=" + recipe.getId() + "\">" + recipe.getTitle() + "</a></td>");

                //get averange rating
                int rating = connection.getAverageRating(recipe.getId());

                //print stars
                out.print("<td>");
                //positive stars
                for (float i = 0; i < rating; i++) {
                    out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\" style=\"color: #f2b600\"></i>");
                }
                //negative stars
                for (float i = rating; i < 5; i++) {
                    out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\"></i>");
                }
                out.print("</td>\n");
                out.println("<td><a href=\"/dashboard?p=delrecipe&rid=" + recipe.getId() + "\">Löschen</a></td>");
                out.println("</tr>");
            }
        %>
    </table>
    <style>

        table, th, td {
            border: 1px solid #DBDBDB;
            border-collapse: collapse;
        }

        th, td {
            padding: 8px;
            text-align: left;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
    </style>


</div>

<div class="footer">
    <%@include file="../templates/footer.jsp" %>
</div>

</body>
</html>

