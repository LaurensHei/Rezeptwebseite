<%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 17.02.2018
  Time: 16:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <title>Mein Account</title>
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/dashboard.css">

    <link rel="stylesheet" href="../css/header.css">
</head>
<body>
<header>
    <%@include file="../templates/header_dashboard.jsp" %>
</header>


<div class="content">
    <h1 class="align-center">Mein Account</h1>

    <form action="/dashboard" method="post" style="width: 400px; margin: 0 auto;">
        <div class="input-field-100">
            <strong>Name</strong>
            <input type="text" name="ch-username" value="${username}">
        </div>
        <div class="input-field-100">
            <strong>E-Mail</strong>
            <input type="email" disabled value="${email}">
        </div>
        <div class="input-field-100">
            <strong>Altes Passwort</strong>
            <input type="password" name="ch-password-old">
        </div>
        <div class="input-field-100">
            <strong>Neues Passwort</strong>
            <input type="password" name="ch-password-new">
        </div>
        <div class="input-field-100">
            <input type="submit" class="button" value="Speichern" style="float: right;">
        </div>
    </form>

</div>

<div class="footer">
    <%@include file="../templates/footer.jsp" %>
</div>

</body>
</html>
