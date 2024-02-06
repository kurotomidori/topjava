<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h1>${meal.id != null ? "Edit Meal" : "Add Meal"}</h1>
<section>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <div>
            <h4>DateTime:</h4>
            <input type="datetime-local" name="date" value="${meal.dateTime}"/>
        </div>
        <div>
            <h4>Description</h4>
            <input type="text" name="description" value="${meal.description}"/>
        </div>
        <div>
            <h4>Calories</h4>
            <input type="number" name="calories" value="${meal.calories}"/>
        </div>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
</body>
</html>
