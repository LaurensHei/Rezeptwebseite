<%
    HttpSession httpSession = request.getSession(false);
%>

<div class="header-content">
    <ul class="header-nav">
        <div class="search-container">
            <form action="/rezepte" method="get" id="search-form">
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
            <a href="/dashboard?p=myaccount">Account</a>
            <a href="/dashboard?p=myrecipes">Meine Rezepte</a>
            <a href="/dashboard?p=createrecipe"><span>Rezept eintragen</span></a>
        </div>
    </div>
</div>