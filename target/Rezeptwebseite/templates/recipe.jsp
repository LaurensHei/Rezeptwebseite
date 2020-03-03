<%@ page import="de.laurens.rezeptwebseite.mysql.MySQLConnection" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 21.01.2018
  Time: 20:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MySQLConnection connection = new MySQLConnection("127.0.0.1", "3306", "kochbuch", "root", "laurens");
%>
<%
    JSONArray informations = new JSONArray((String) request.getAttribute("information_set"));
    String time = informations.getJSONObject(0).getString("time");
    String kcal = informations.getJSONObject(1).getString("kcal");
    String persons = informations.getJSONObject(2).getString("persons");

%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <link rel="icon" href="../img/icon.png">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/recipe.css">
    <link rel="stylesheet" href="../css/header.css">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>${title}</title>

    <script src="../jquery/jquery-3.1.1.min.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {
            $('.rating-star').change(function () {
                var ratingValue = $(this).val();

                var params = {
                    ratingInput: ratingValue,
                    recipeID: '${id}',
                    userID: '${user}'
                };
                $.ajax({
                    url: 'rezepte',
                    type: 'POST',
                    dataType: 'json',
                    data: params,
                    success: function (data) {
                        $('input[value=' + ratingValue + ']').checked = true;
                    }
                });
                return false;
            });
        });
    </script>
</head>
<body>
<header>
    <%@include file="header.jsp" %>
</header>
<div class="content">
    <div class="recipe-section">
        <div id="title-section" class="section">
            <h1>${title}</h1>
            <div class="informations">

                <div class="star-rating" id="star-rating">
                    <input id="star-5" type="radio" name="rating" value="5" class="rating-star">
                    <label for="star-5">
                        <i class="active fa fa-star" aria-hidden="true"></i>
                    </label>
                    <input id="star-4" type="radio" name="rating" value="4" class="rating-star">
                    <label for="star-4">
                        <i class="active fa fa-star" aria-hidden="true"></i>
                    </label>
                    <input id="star-3" type="radio" name="rating" value="3" class="rating-star">
                    <label for="star-3">
                        <i class="active fa fa-star" aria-hidden="true"></i>
                    </label>
                    <input id="star-2" type="radio" name="rating" value="2" class="rating-star">
                    <label for="star-2">
                        <i class="active fa fa-star" aria-hidden="true"></i>
                    </label>
                    <input id="star-1" type="radio" name="rating" value="1" class="rating-star">
                    <label for="star-1">
                        <i class="active fa fa-star" aria-hidden="true"></i>
                    </label>
                </div>
                <label id="star-rating-label" for="star-rating"></label>


                <script>
                    window.onload = function () {
                        var rating = ${rating};
                        var own_rating = ${own_rating};
                        var star = document.getElementById("star-" + rating);
                        star.checked = true;
                        if (own_rating == true) {
                            document.getElementById('star-rating-label').innerHTML = 'Eigene Bewertung';
                        } else {
                            document.getElementById('star-rating-label').innerHTML = 'Durchschnittsbewertung';
                        }

                    }
                </script>

                <div class="time">
                    <img style="vertical-align: middle;" src="../img/time.png" alt="">
                    <%out.println("<span style=\"vertical-align: middle\">" + time + "</span>");%>
                </div>

                <div class="kcal">
                    <img style="vertical-align: middle;" src="../img/time.png" alt="">
                    <%out.println("<span style=\"vertical-align: middle\">" + kcal + " Kcal</span>");%>
                </div>

                <div class="persons">
                    <img style="vertical-align: middle;" src="../img/time.png" alt="">
                    <%out.println("<span style=\"vertical-align: middle\">" + persons + " Personen</span>");%>
                </div>
            </div>
        </div>
        <div id="ingredients-section" class="section">
            <div style="margin-bottom: 28px;">
                <div class="button-group" style="float: right;">
                    <button onclick="enableTexted()"><img src="../img/text_view.png" alt="" style="width: 24px">
                    </button>
                    <button onclick="enableImaged()"><img src="../img/image_view.png" alt="" style="width: 24px">
                    </button>
                </div>
                <h3 style="display: inline-block; margin-bottom: 0">Zutaten</h3>
            </div>
            <div class="ingredient-img-container " id="ingredient-img-container"
                 style="-moz-user-select: none; -webkit-user-select: none; -ms-user-select:none; user-select:none;-o-user-select:none;"
                 unselectable="on"
                 onselectstart="return false;"
                 onmousedown="return false;">
                <%
                    JSONArray jsonarray = new JSONArray((String) request.getAttribute("ingredients"));
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String ingredient = jsonobject.getString("ingredient");
                        String amount = jsonobject.getString("amount");

                        out.println("<div class=\"ingredient\">");
                        out.println("<img src=\"../img/" + connection.getIngredientImagePath(ingredient) + "\" alt=\"img\">");
                        out.println("<h5 class=\"ingredient-description\">" + ingredient + "</h5>");
                        out.println("<h4>" + amount + "</h4>");
                        out.println("</div>");
                    }
                %>
            </div>
            <div class="ingredient-text-container" id="ingredient-text-container">
                <table id="ingredient-table">
                    <tr>
                        <th>Zutat</th>
                        <th>Menge</th>
                    </tr>
                    <%
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String ingredient = jsonobject.getString("ingredient");
                            String amount = jsonobject.getString("amount");
                            out.println("<tr>");
                            out.println("<td>" + ingredient + "</td>");
                            out.println("<td>" + amount + "</td>");
                            out.println("</tr>");
                        }
                    %>
                </table>
            </div>
        </div>
    </div>
    <div id="instruction-section" class="section">
        <h3>Zubereitung</h3>
        <p>${instruction}</p>
    </div>
    <div class="clear"></div>
    <div class="full-seperator"></div>
</div>

<div class="footer">
    <%@include file="footer.jsp" %>
</div>


</body>

<script>
    function enableImaged() {
        document.getElementById("ingredient-img-container").style.display = "flex";
        document.getElementById("ingredient-text-container").style.display = "none";
    }

    function enableTexted() {
        document.getElementById("ingredient-img-container").style.display = "none";
        document.getElementById("ingredient-text-container").style.display = "block";
    }

</script>

</html>