<%--
  Created by IntelliJ IDEA.
  User: laure
  Date: 13.02.2018
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>News</title>
    <meta name="viewport" content="width=device-width, init-scale=1.0">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/news.css">
</head>
<body>
<header>
    <%@include file="../templates/header.jsp" %>
</header>


<div class="content">
    <h1>News</h1>
    <div class="news-content">
        <h1>Neue Funktionen</h1>
        <h4 class="timestamp">13.02.18</h4>
        <img src="../img/news/news_1_head.jpg" alt="" style="width: 500px;">
        <p>Hallo User,<br>
            Wir haben 2 neue Funktionen eingeführt:<br><br>
            -Funktion 1<br>
            -Funktion 2<br><br>
            Beide Funktionen funktionieren einwandfrei. Sollten dennoch Bugs auftauchen, bitten wir Dich darum,
            uns eine E-Mail zu schreiben und uns dort den genauen Bug zu erläutern.<br>
            Bis dahin wünschen wir Dir viel Spaß mit den neuen Funktionen und hoffen, dass sie auch funktionieren.
            <br><br>
            Deine Funktionsexperten :)<br><br>
            Hier noch ein paar Zeilen, damit an der Seite eine Scrollbar erscheint :)<br><br>-<br>-<br>-<br>-<br>-<br>-<br>-<br>-<br>-</p>
    </div>

    <div class="news-list">
        <div class="news-item">
            <div class="news-description">
                <h3>Neue Funktionen</h3>
                <h4>13.02.18</h4>
            </div>
            <img class="news-thumbnail" src="../img/news/news_thumbnail_1.jpg" alt="">
        </div>
        <div class="news-item">
            <div class="news-description">
                <h3>Bugfixes</h3>
                <h4>09.02.18</h4>
            </div>
            <img class="news-thumbnail" src="../img/news/news_thumbnail_2.jpg" alt="">
        </div>
        <div class="news-item">
            <div class="news-description">
                <h3>Änderungen am Design</h3>
                <h4>31.01.18</h4>
            </div>
            <img class="news-thumbnail" src="../img/news/news_thumbnail_3.jpg" alt="">
        </div>
        <div class="news-item">
            <div class="news-description">
                <h3>Neue Ideen für das Projekt</h3>
                <h4>18.12.17</h4>
            </div>
            <img class="news-thumbnail" src="../img/news/news_thumbnail_4.jpg" alt="">
        </div>
    </div>
</div>
</body>
</html>
