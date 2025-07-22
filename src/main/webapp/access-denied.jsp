<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>禁止访问 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
  <style>
    .center-container { display: flex; align-items: center; justify-content: center; height: 100vh; }
  </style>
</head>
<body id="page-top">
<div class="container center-container">
  <div class="col-md-8 text-center">
    <div class="card card-glass p-4">
      <div class="card-body">
        <h1 class="display-1 text-danger"><i class="bi bi-exclamation-octagon-fill"></i></h1>
        <h2 class="text-danger">403 - 禁止访问 (Access Denied)</h2>
        <p class="lead mt-3">
          抱歉，您没有足够的权限访问此页面。
        </p>
        <p class="font-weight-bold">
          所需权限: <c:out value="${message}" />
        </p>
        <hr>
        <p class="mb-0">
          <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">返回仪表盘</a>
          <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">退出登录</a>
        </p>
      </div>
    </div>
  </div>
</div>
</body>
</html>