<%--
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
    <title>Rezept eintragen</title>
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/dashboard.css">
</head>
<body>

<header>
    <%@include file="../templates/header_dashboard.jsp" %>
</header>


<div class="content">
    <h1 class="align-center">Rezept eintragen</h1>


    <div class="wrapper">
        <div class="input-field-100">
            <strong style="margin-top: 20px">Titel</strong>
            <input type="text" id="title" required/>
        </div>

        <div class="input-field-100">
            <strong style="margin-top: 20px">Informationen</strong>
            <input type="text" id="time" placeholder="Minuten" style="width: 10%;" required>
            <input type="text" id="kcal" placeholder="kcal" style="width: 10%;" required>
            <input type="text" id="persons" placeholder="Personen" style="width: 10%;" required>
            <input type="text" id="category" placeholder="Kategorie" style="width: 15%;" required>
        </div>

        <div class="input-field-100">
            <strong style="margin-top: 20px">Zutaten</strong>
            <div class="ingredients-container">
                <input type="text" id="ingredient-input" style="max-width: 55%; margin-right: 4%">
                <input type="text" id="ingredient-amount" style="max-width: 10%;">
                <select name="Menge" id="ingredient-unit" style="max-width: 20%; margin-right: 3%;">
                    <option value="Bund">Bund</option>
                    <option value="Centiliter">Centiliter</option>
                    <option value="Deziliter">Deziliter</option>
                    <option value="Esslöffel">Esslöffel</option>
                    <option value="Gramm">Gramm</option>
                    <option value="Kilogramm">Kilogramm</option>
                    <option value="Liter">Liter</option>
                    <option value="Mass">Mass</option>
                    <option value="Messerspitze">Messerspitze</option>
                    <option value="Milliliter">Milliliter</option>
                    <option value="Milligramm">Milligramm</option>
                    <option value="Pfund">Pfund</option>
                    <option value="Prise">Prise</option>
                    <option value="Scheibe">Scheibe</option>
                    <option value="Schuss">Schuss</option>
                    <option value="Spritzer">Spritzer</option>
                    <option value="Stück">Stück</option>
                    <option value="Tasse">Tasse</option>
                    <option value="Teelöffel">Teelöffel</option>
                    <option value="Tropfen">Tropfen</option>
                </select>
                <button class="button button-ok" id="add-ingredient"
                        style="max-width: 5%; padding-right:0; padding-left: 0;">Ok
                </button>
            </div>

            <table id="ingredient-table">
                <tr>
                    <th>Zutat</th>
                    <th>Menge</th>
                    <th>
                        <button class="table-x" disabled>X</button>
                    </th>
                </tr>
            </table>
        </div>

        <div class="input-field-100">
            <strong style="margin-top: 20px">Zubereitung</strong>
            <textarea id="instruction" cols="30" rows="10" required></textarea>
        </div>

        <div class="input-field-100">

            <form action="/dashboard" method="post" style="height: auto; overflow: hidden" id="final-form">

                <div style="display: none; background-color: red;">
                    <input type="text" id="cr-title" name="cr-title">
                    <input type="text" id="cr-ingredients" name="cr-ingredients">
                    <input type="text" id="cr-instruction" name="cr-instruction">
                    <input type="text" id="cr-keywords" name="cr-keywords">
                    <input type="text" id="cr-category" name="cr-category">
                    <input type="text" id="cr-information-set" name="cr-information-set">
                </div>

                <input class="button" type="submit" id="submit" value="Rezept veröffentlichen"
                       style="float: right; width: auto;"/>
            </form>
        </div>
    </div>
</div>

<div class="footer">
    <%@include file="../templates/footer.jsp" %>
</div>


<script>
    document.getElementById('add-ingredient').onclick = function () {
        //getting values from input
        var ingredient_name = document.getElementById('ingredient-input');
        var ingredient_amount = document.getElementById('ingredient-amount');
        var ingredient_unit = document.getElementById('ingredient-unit');
        var str_unit = ingredient_unit.options[ingredient_unit.selectedIndex].value;

        //getting table
        var table = document.getElementById('ingredient-table');

        //create row
        var row = table.insertRow(1);

        //Insert new cells into the row
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        var cell3 = row.insertCell(2);

        //fill the new cells with the input above
        cell1.innerHTML = ingredient_name.value;
        cell2.innerHTML = ingredient_amount.value + ' ' + str_unit;
        cell3.innerHTML = '<button class="table-x" onclick="deleteRow(this)">X</button>';
        /*cell3.onclick = deleteRow(this);*/

        //clear the input fields
        ingredient_name.value = '';
        ingredient_amount.value = '';
    };

    function deleteRow(r) {
        var i = r.parentNode.parentNode.rowIndex;
        document.getElementById("ingredient-table").deleteRow(i);
    };

    document.getElementById('submit').onclick = function () {
        //getting values from input
        const title = document.getElementById('title');
        const ingredients = JSON.stringify(tableToArray(document.getElementById('ingredient-table')));
        const instruction = document.getElementById('instruction');
        const keywords = JSON.stringify(document.getElementById('keywords'));
        const category = document.getElementById('category').value;

        //information values
        const time = document.getElementById('time').value;
        const kcal = document.getElementById('kcal').value;
        const persons = document.getElementById('persons').value;
        const informationValues = [time, kcal, persons];


        document.getElementById('cr-title').value = title.value;
        document.getElementById('cr-ingredients').value = ingredients;
        document.getElementById('cr-instruction').value = instruction.value.replace(/\n/gm, '%n%');
        document.getElementById('cr-keywords').value = keywords;
        document.getElementById('cr-category').value = category;
        document.getElementById('cr-information-set').value = informationsToJSONArray(informationValues);
    };

    document.getElementById('final-form').onsubmit = function () {
        return checkform();
    };


    function checkform() {
        var inputs = document.getElementsByTagName('input');
        for (var i = 0; i < inputs.length; i++) {

            if (inputs[i].hasAttribute("required")) {
                if (inputs[i].value === "") {
                    inputs[i].style.border = '1px solid red';
                    return false;
                }
            }
        }
        return true;
    }

    function informationsToJSONArray(array) {
        //time, kcal, persons, personcalc bool
        var result = [];

        var time = {"time": array[0]};
        result.push(time);

        var kcal = {"kcal": array[1]};
        result.push(kcal);

        var persons = {"persons": array[2]};
        result.push(persons);

        var personcalc = {"personcalc": array[3]};
        result.push(personcalc);

        var json = JSON.stringify(result);
        return json;
    }

    function tableToArray(table) {
        var result = [];
        var rows = table.rows;
        var cells;

        for (var i = 1, iLen = rows.length; i < iLen; i++) {
            cells = rows[i].cells;
            var item = {
                "ingredient": cells[0].innerHTML,
                "amount": cells[1].innerHTML
            };
            result.push(item);
        }
        return result;
    }

</script>

</body>
</html>

