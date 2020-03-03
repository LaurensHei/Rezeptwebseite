<%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 04.02.2018
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <title>Login</title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/login.css">
    <link rel="stylesheet" href="../css/header.css">

    <script src="../jquery/jquery-3.1.1.min.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {
            $('#login-form').submit(function () {
                var email = document.getElementById('email').value;
                var password = document.getElementById('password').value;

                if (!email.includes('@')) {
                    $('#email').css("border", "1px solid red");
                    $('#email-label').html('Ungültige E-Mail');
                    return false;
                }

                var params = {
                    email: email,
                    password: password
                };
                $.ajax({
                    url: 'login',
                    type: 'POST',
                    dataType: 'json',
                    data: params,
                    success: function (data) {
                        if (data.isValid) {
                            window.location = "../index.jsp";
                        } else {
                            document.getElementById('password').value = "";
                            $(".login-input").css("border", "1px solid red");
                            $("#password-label").html("Email oder Passwort falsch!");
                        }
                    }
                });
                return false;
            });
        });
    </script>

</head>
<body>

<video autoplay muted loop id="background-video">
    <source src="../video/signup-loop.mp4" type="video/mp4">
    Your browser does not support HTML5 video.
</video>

<div class="content">
    <div class="login-container">
        <form id="login-form" class="login-form">
            <div class="login-title">
                <h1>Login</h1>
            </div>
            <div class="input-wrapper">
                <strong>E-Mail</strong>
                <input type="text" name="email" id="email" class="login-input">
                <label for="email" id="email-label"></label>
            </div>
            <div class="input-wrapper">
                <strong>Passwort</strong>
                <input type="password" name="password" id="password" class="login-input">
                <label for="password" id="password-label"></label>
            </div>
            <div class="button-wrapper">
                <button class="button" type="submit" style="display: inline-block;">Login</button>
            </div>

            <a href="/signup" style="position: absolute; bottom: 10px; right: 10px;"><span>Registrieren</span></a>
        </form>
    </div>
</div>

</body>
</html>
