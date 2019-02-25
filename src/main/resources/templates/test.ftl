<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h3>
    <#list vos as vo>
        src= ${vo.get('news').getLink()}<br>
    </#list>
</h3>
</body>
</html>