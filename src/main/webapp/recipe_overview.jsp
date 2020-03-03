<%@ page import="de.laurens.rezeptwebseite.servlets.recipe.MinRecipe" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 10.05.2018
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rezepte suchen</title>
    <meta charset="utf-8">

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/recipe_overview.css">
    <link rel="stylesheet" href="css/header.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

    <script src="jquery/jquery-3.1.1.min.js" type="text/javascript"></script>

</head>
<body>

<header>
    <%@include file="templates/header.jsp" %>
</header>

<div class="content">
    <div class="grid-container">
        <div class="head-section">
            <h3>Rezepte suchen</h3>
        </div>

        <div class="sidebar">
            <h3>Kategorien</h3>

            <div class="category-wrapper">
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Aufläufe</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Cocktails</a>
                <a href="/rezepte?category=default" style="font-size: 18px;" class="category-list-item">Default</a>
                <a href="/rezepte?category=desserts" style="font-size: 18px;" class="category-list-item">Desserts</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Eisgerichte</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Fingerfood</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Frühstück</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Grillrezepe</a>
                <a href="/rezepte?category=nudelgerichte" style="font-size: 18px;" class="category-list-item">Nudelgerichte</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Salat</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Suppen</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Thermomix</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Torten</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Vegan</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Vegetarisch</a>
                <a href="/rezepte?category=eisgerichte" style="font-size: 18px;" class="category-list-item">Vorspeisen</a>


            </div>
        </div>

        <div class="main-section">
            <h3 id="search-display"></h3>

            <div class="recipe-box-wrapper" id="recipe-box-wrapper">
                <%
                    try {
                        ArrayList<MinRecipe> collection = (ArrayList<MinRecipe>) request.getAttribute("results");
                        Iterator<MinRecipe> iterator = collection.iterator();

                        while (iterator.hasNext()) {
                            MinRecipe recipe = iterator.next();

                            JSONArray informations = new JSONArray(recipe.getInformationSet());
                            String time = informations.getJSONObject(0).getString("time");
                            String kcal = informations.getJSONObject(1).getString("kcal");
                            String persons = informations.getJSONObject(2).getString("persons");


                            out.println("<div class=\"recipe-list-box\">\n");
                            out.println("<div class=\"img-wrapper\"></div>");
                            out.println("<div class=\"main-wrapper\">\n");
                            out.println("<a href=\"/rezepte?id=" + recipe.getId() + "\"><h5 class=\"inline-block recipe-box-title\">" + recipe.getTitle() + "</h5></a>\n");
                            out.println("<div class=\"clear\"></div>\n");
                            out.println("<p class=\"inline-block recipe-box-description\">Kategorie: " + recipe.getCategory() + "</p>\n");
                            out.println("</div>\n");
                            out.println("<div class=\"information-wrapper\">\n");
                            out.println("<p class=\"inline-block\" style=\"margin: 0; padding-left: 10px;\">\n");

                            int stars = 0;
                            for (int i = 0; i < recipe.getRating(); i++) {
                                out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\" style=\"color: #f2b600;\"></i>\n");
                                stars++;
                            }
                            for (int i = stars; i < 5; i++) {
                                out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\"></i>\n");
                            }
                            out.println("</p>");
                            out.println("<div class=\"clear\"></div>");
                            out.println("<p style=\"margin: 0; padding-left: 10px; font-size: 1rem;\">Zeit: <span>" + time + "</span></p>");
                            out.println("<p style=\"margin: 0; padding-left: 10px; font-size: 1rem;\">Kcal: <span>" + kcal + "</span></p>");
                            out.println("<p style=\"margin: 0; padding-left: 10px; font-size: 1rem;\">Personen: <span>" + persons + "</span></p>");
                            out.println("</div>");
                            out.println("</div>");
                        }
                    } catch (NullPointerException ex) {
                    }
                %>
            </div>
            <a href=""></a>

            <div style="position: absolute; width: 60%; bottom: 20px;">
                <div class="page-switcher-wrapper">
                    <%
                        if (request.getParameterMap().containsKey("search") || request.getParameterMap().containsKey("category")) {
                            Integer count = (Integer) request.getAttribute("count");
                            int sites = (int) Math.ceil(count / 4f);
                            Integer currentSite = (Integer) request.getAttribute("page");

                            String parameter;
                            if (request.getParameterMap().containsKey("search")) {
                                parameter = "search";
                            } else {
                                parameter = "category";
                            }

                            out.println(currentSite > 1 ? "<a href=\"/rezepte?" + parameter + "=" + request.getParameter(parameter) + "&page=" + (currentSite - 1) + "\"><button class=\"move-back\">&lt;</button></a>" : "<button class=\"move-back\" disabled>&lt;</button>");
                            out.println("<p class=\"inline-block\" style=\"margin-right: 5px; margin-left: 5px;\">Seite " + currentSite + " von " + sites + "</p>");
                            out.println(currentSite < sites ? "<a href=\"/rezepte?" + parameter + "=" + request.getParameter(parameter) + "&page=" + (currentSite + 1) + "\"><button class=\"move-forth\">&gt;</button></a>" : "<button class=\"move-forth\" disabled>&gt;</button>");
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="footer">
    <%@include file="templates/footer.jsp" %>
</div>


</body>
</html>