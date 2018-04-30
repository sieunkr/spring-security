<html>
<head>
</head>
<body>
<h1>리스트 페이지</h1>
본문 리스트 페이지
<form name='f' action="cafe_logout" method='POST'>
    <input type="submit" value="Logout"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>