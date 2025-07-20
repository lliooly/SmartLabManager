<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/20
  Time: 08:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>验证邮箱</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <%-- 引入与login.jsp相同的CSS样式 --%>
</head>
<body class="text-center">
<form class="login-form" action="verify-registration" method="post">
  <h1 class="h3 mb-3 font-weight-normal">验证您的邮箱</h1>
  <p class="text-muted">我们已向您的注册邮箱发送了一个6位数验证码，请在下方输入以完成注册。</p>
  <c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}"/></div>
  </c:if>
  <div class="form-group">
    <label for="verificationCode" class="sr-only">验证码</label>
    <input type="text" id="verificationCode" name="verificationCode" class="form-control" placeholder="6位验证码" required autofocus>
  </div>
  <button class="btn btn-lg btn-primary btn-block" type="submit">验证并完成注册</button>
</form>
</body>
</html>
