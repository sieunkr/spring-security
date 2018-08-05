<html>
<head>
</head>
<body>
<h1>리스트 페이지</h1>

사용자 정보 = ${user}


<form name='f' action="/logout" method='POST'>
    <input type="submit" value="Logout"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>