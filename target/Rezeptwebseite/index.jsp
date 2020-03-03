<%@ page import="de.laurens.rezeptwebseite.mysql.MySQLConnection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="de.laurens.rezeptwebseite.servlets.recipe.Recipe" %>
<%@ page import="de.laurens.rezeptwebseite.servlets.recipe.MinRecipe" %>
<%@ page import="org.json.JSONArray" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <title>Rezeptwebseite | Index</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/header.css">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="jquery/jquery-3.1.1.min.js" type="text/javascript"></script>

    <!-- Icon -->
    <link rel="icon" href="img/icon.png">
</head>
<body>

<div class="landing">
    <section id="img-section">
        <header>
            <%@include file="templates/header.jsp" %>
        </header>
        <div id="img-section-wrapper">
            <!--<h1 id="title">REZEPTWEBSEITE</h1>-->

            <a class="image-block" href="#top-recipes-section">
                <svg class="arrows" id="arrows">
                    <path class="a1" d="M0 0 L30 32 L60 0"></path>
                    <path class="a2" d="M0 20 L30 52 L60 20"></path>
                    <path class="a3" d="M0 40 L30 72 L60 40"></path>
                </svg>
            </a>
        </div>
    </section>

    <section id="top-recipes-section">
        <div class="padding">
            <h3>Top-Rezepte</h3>
            <div class="recipe-card-wrapper">
                <%
                    MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
                    ArrayList<MinRecipe> recipes = connection.getBestRatedRecipes(5, 0);
                    int order = 0;
                    for (MinRecipe recipe : recipes) {
                        order++;

                        JSONArray informations = new JSONArray(recipe.getInformationSet());
                        String time = informations.getJSONObject(0).getString("time");
                        String kcal = informations.getJSONObject(1).getString("kcal");
                        String persons = informations.getJSONObject(2).getString("persons");

                        out.println("<a href=\"rezepte?id=" + recipe.getId() + "\">");
                        out.println("<div class=\"recipe-card inline-block\">");
                        out.println("<div class=\"card-top\">");
                        out.println("<h5 class=\"align-center\" style=\"margin-top: 20px; margin-bottom: 0px;\">" + recipe.getTitle() + "</h5>");
                        out.println("<div class=\"align-center\">");
                        int rating = recipe.getRating();
                        for (int i = 0; i < rating; i++) {
                            out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\" style=\"color: #f2b600;\"></i>");
                        }
                        for (int i = rating; i < 5; i++) {
                            out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\"></i>");
                        }
                        out.println("</div>\n");
                        out.println("<h1 class=\"align-center\" style=\"margin-top: 10px;\">" + order + "</h1>");
                        out.println("</div>");
                        out.println("<div class=\"card-bottom\">");
                        out.println("<div class=\"card-informations\">");
                        out.println("<p>Zeit: <span>" + time + "</span></p>");
                        out.println("<p>Kcal: <span>" + kcal + "</span></p>");
                        out.println("<p>Personen: <span>" + persons + "</span></p>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</a>");

                    }
                %>
            </div>
        </div>
    </section>

    <section id="last-published-section">
        <div class="padding">
            <h3>Zuletzt veröffentlicht</h3>
            <div class="recipe-card-wrapper">
                <%
                    ArrayList<MinRecipe> lastRecipes = connection.getLastPublishedRecipes(5, 0);
                    for (MinRecipe recipe : lastRecipes) {

                        JSONArray informations = new JSONArray(recipe.getInformationSet());
                        String time = informations.getJSONObject(0).getString("time");
                        String kcal = informations.getJSONObject(1).getString("kcal");
                        String persons = informations.getJSONObject(2).getString("persons");

                        out.println("<a href=\"rezepte?id=" + recipe.getId() + "\">");
                        out.println("<div class=\"recipe-card inline-block\">");
                        out.println("<div class=\"card-top\">");
                        out.println("<h5 class=\"align-center\" style=\"margin-top: 20px; margin-bottom: 0px;\">" + recipe.getTitle() + "</h5>");
                        out.println("<div class=\"align-center\">");
                        int rating = recipe.getRating();
                        for (int i = 0; i < rating; i++) {
                            out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\" style=\"color: #f2b600;\"></i>");
                        }
                        for (int i = rating; i < 5; i++) {
                            out.println("<i class=\"active fa fa-star\" aria-hidden=\"true\"></i>");
                        }
                        out.println("</div>\n");
                        out.println("</div>");
                        out.println("<div class=\"card-bottom\">");
                        out.println("<div class=\"card-informations\">");
                        out.println("<p>Zeit: <span>" + time + "</span></p>");
                        out.println("<p>Kcal: <span>" + kcal + "</span></p>");
                        out.println("<p>Personen: <span>" + persons + "</span></p>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</div>");
                        out.println("</a>");

                    }
                %>
            </div>
        </div>
    </section>
</div>

<div class="footer">
    <%@include file="templates/footer.jsp" %>
</div>

<script>
    // Select all links with hashes
    $('a[href*="#"]')
    // Remove links that don't actually link to anything
        .not('[href="#"]')
        .not('[href="#0"]')
        .click(function (event) {
            // On-page links
            if (
                location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '')
                &&
                location.hostname == this.hostname
            ) {
                // Figure out element to scroll to
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                // Does a scroll target exist?
                if (target.length) {
                    // Only prevent default if animation is actually gonna happen
                    event.preventDefault();
                    $('html, body').animate({
                        scrollTop: target.offset().top
                    }, 1000, function () {
                    });
                }
            }
        });

    window.onscroll = function () {
        scrollFunction()
    };

    function scrollFunction() {
        if (document.body.scrollTop > 500 || document.documentElement.scrollTop > 500) {
            document.getElementById("arrows").style.display = "none";
        } else {
            document.getElementById("arrows").style.display = "inline-block";
        }
    }
</script>

<script>
    var imgsection = $('#img-section-wrapper');
    var range = 200;

    $(window).on('scroll', function () {

        var scrollTop = $(this).scrollTop(),
            height = imgsection.outerHeight(),
            offset = height / 2,
            calc = 1 - (scrollTop - offset + range) / range;

        imgsection.css({'opacity': calc});

        if (calc > '1') {
            imgsection.css({'opacity': 1});
        } else if (calc < '0') {
            imgsection.css({'opacity': 0});
        }

        if (imgsection.css('opacity') == 0) {
            imgsection.hide();
        }else {
            imgsection.show();
        }
    });
</script>

</body>
