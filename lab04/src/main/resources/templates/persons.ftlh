<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Persons</title>
</head>
<body>

<h2>Список людей</h2>

<table border="1">

    <tr>
        <th>ID</th>
        <th>Name</th>
    </tr>

    <#list persons as p>
        <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
        </tr>
    </#list>

</table>

<br>

<a href="/persons/new">Создать нового пользака</a>

</body>
</html>