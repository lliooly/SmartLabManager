<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/19
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>禁止访问 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container">
  <div class="row justify-content-center mt-5">
    <div class="col-md-6 text-center">
      <div class="alert alert-danger">
        <h1 class="display-1">403</h1>
        <h2>禁止访问 (Access Denied)</h2>
        <p class="lead">
          抱歉，您没有足够的权限访问此页面。
        </p>
        <p>
          <strong><c:out value="${message}" /></strong>
        </p>
        <hr>
        <p class="mb-0">
          <a href="${pageContext.request.contextPath}/tasks" class="btn btn-primary">返回主页</a>
          <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">退出登录</a>
        </p>
      </div>
    </div>
  </div>
</div>
</body>
</html>