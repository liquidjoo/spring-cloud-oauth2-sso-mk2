<html>
<head>
</head>
<body>
<div class="container">
    <form role="form" action="login" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username"/>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password"/>
        </div>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>

<div>
    <a class="facebook-login-text" href="/mk-auth/oauth2/authorization/facebook">facebook 로그인</a>
</div>

<div>
    <a class="google-login-text" href="/mk-auth/oauth2/authorization/google">google 로그인</a>
</div>
</body>
</html>