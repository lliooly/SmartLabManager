<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>登录 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <style>
    body { display: flex; align-items: center; justify-content: center; height: 100vh; background-color: #f5f5f5; }
    .login-form { width: 100%; max-width: 330px; padding: 15px; margin: auto; }
  </style>
</head>
<body class="text-center">
<form class="login-form" action="login" method="post">
  <h1 class="h3 mb-3 font-weight-normal">请登录</h1>

  <%-- 用于显示注册成功后的提示 --%>
  <c:if test="${param.reg == 'success'}">
    <div class="alert alert-success">注册成功！请使用您的新账户登录。</div>
  </c:if>

  <%-- 用于显示登录失败后的错误信息 --%>
  <c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert">
      <c:out value="${error}" />
    </div>
  </c:if>

  <div class="form-group">
    <label for="username" class="sr-only">用户名</label>
    <input type="text" id="username" name="username" class="form-control" placeholder="用户名" required autofocus>
  </div>

  <div class="form-group">
    <label for="password" class="sr-only">密码</label>
    <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
  </div>

  <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>

  <div class="text-center mt-3">
    <p>还没有账户？ <a href="register">立即注册</a></p>
  </div>

  <p class="mt-5 mb-3 text-muted">&copy; 2025 智能实验室</p>
</form>
</body>
</html>