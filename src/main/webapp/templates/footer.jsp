<%@ page import="de.laurens.rezeptwebseite.secure.User" %>

<p class="align-center inline-block" style="margin: 0 0 0 20px; line-height: 50px;">&copy;Laurens Heithecker</p>

<%
    httpSession = request.getSession(false);
    if (httpSession != null) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            out.println("<a href=\"/logout\" style=\"float: right;\"><span style=\"line-height: 50px; margin-right: 20px;\">Abmelden</span></a>");
        }
    }

%>
