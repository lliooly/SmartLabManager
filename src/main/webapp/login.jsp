<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>登录 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
  <style>
    .login-container { display: flex; align-items: center; justify-content: center; height: 100vh; }
    .login-form-container { width: 100%; max-width: 400px; }
  </style>
</head>
<body id="page-top">
<div class="login-container">
  <div class="login-form-container">
    <div class="card card-glass p-4">
      <div class="card-body">
        <h3 class="card-title text-center mb-4"><i class="bi bi-box-arrow-in-right"></i> 登录智能实验室平台</h3>
        <form action="login" method="post">
          <%-- 提示信息 --%>
          <c:if test="${param.reg == 'success'}">
            <div class="alert alert-success">注册成功！请登录。</div>
          </c:if>
          <c:if test="${not empty error}">
            <div class="alert alert-danger"><c:out value="${error}" /></div>
          </c:if>

          <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" name="username" class="form-control" required autofocus>
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password" class="form-control" required>
          </div>
          <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
          <div class="text-center mt-3">
            <a href="register">还没有账户？立即注册</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
</body>
</html>