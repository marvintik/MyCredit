<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-16">
    <title>Пирвет ${name}!</title>
    <link href="/css/main.css" rel="stylesheet">
</head>
                         <h3><a href="/api/v1/persons">Люди</a></h3>
                         <h3><a href="/api/v1/credits">Кредиты</a></h3>
<body>
        <table border="1" cellpadding="8">
                <tr>
                    <th>ID</th>
                    <th>Банк</th>
                    <th>Название</th>
                    <th>Дата начала</th>
                    <th>Дата окончания</th>
                    <th>Сумма</th>
                </tr>
    <script src="/js/main.js"></script>
        <c:forEach items="${listCredits}" var="credit">
        <tr>
            <td>${credit.id}</td>
            <td>${credit.bank}</td>
            <td>${credit.title}</td>
            <td>${credit.date_start}</td>
            <td>${credit.date_end}</td>
            <td>${credit.cost}</td>
        </tr>
        </c:forEach>
            </table>
</body>
</html>