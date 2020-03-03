<%@ page import="de.laurens.rezeptwebseite.secure.User" %>

<div class="header-content">
    <ul class="header-nav">
        <div class="search-container">
            <form action="/rezepte" mehod="get" id="search-form">
                <input type="text" class="search" id="search" name="search" placeholder="Suche...">
                <img class="search-img" src="img/search_icon.png" alt="">
                <input type="submit" class="button-min" id="search-enter" value="Suchen">
            </form>
            <script>
                document.getElementById('search-form').onsubmit = function () {
                    return isValidSearch();
                }
                function isValidSearch() {
                    var input = document.getElementById('search').value;
                    if (input === "") {
                        return false;
                    } else {
                        return true;
                    }
                }
            </script>
        </div>
    </ul>

    <div class="nav">
        <label class="toggle" for="toggle">&#9776;</label>
        <input type="checkbox" id="toggle">
        <div class="menu">
            <a href="/rezepte">Rezepte</a>
            <a href="/rezepte?id=r">Zufallsrezept</a>

            <%
                HttpSession httpSession = request.getSession(false);
                if (httpSession != null) {
                    User user = (User) httpSession.getAttribute("user");
                    if (user != null) {
                        out.println("<a href=\"/dashboard\"><span>Dashboard</span></a>");
                    } else {
                        out.println("<a href=\"/login\"><span>Login</span></a>");
                    }
                } else {
                    out.println("<a href=\"/login\"><span>Login</span></a>");
                }
            %>
        </div>
    </div>
</div>
