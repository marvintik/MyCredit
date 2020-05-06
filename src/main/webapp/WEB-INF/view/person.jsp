<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-16">
    <title>Пирвет ${name}!</title>
    <link href="/css/main.css" rel="stylesheet">
</head>
                         <h3><a href="/api/v1/person">Люди</a></h3>
                         <h3><a href="/api/v1/credits">Кредиты</a></h3>
                         <h3><a href="/api/v1/persons/new">Новый пользователь</a></h3>
<body>
        <table border="1" cellpadding="8">
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Фото</th>
                </tr>
    <script src="/js/main.js"></script>
        <c:forEach items="${listPerson}" var="person">
        <tr>
            <td>${person.id}</td>
            <td>${person.name}</td>
            <td>${person.image}</td>
        </tr>
        </c:forEach>
            </table>
</body>
</html>