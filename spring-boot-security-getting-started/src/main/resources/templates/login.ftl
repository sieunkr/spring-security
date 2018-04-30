<html>
<head>
</head>
<body>
<h1>커스텀 로그인 페이지</h1>
<form name='f' action="cafe_login" method='POST'>
    <table>
        <tr>
            <td>User:</td>
            <td>
                <input type='text' name='username' value=''>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td>
                <input type='password' name='password' />
            </td>
        </tr>
        <tr>
            <td>
                <input name="submit" type="submit" value="로그인" />
            </td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>