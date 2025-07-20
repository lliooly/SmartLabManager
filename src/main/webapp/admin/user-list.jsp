<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/19
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>用户管理 - 管理后台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3>用户管理</h3>
    <a href="${pageContext.request.contextPath}/admin/roleList" class="btn btn-info">角色与权限管理</a>
  </div>
  <table class="table table-hover table-bordered">
    <thead class="thead-light">
    <tr>
      <th>ID</th>
      <th>用户名</th>
      <th>全名</th>
      <th>邮箱</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${empty userList}">
      <tr>
        <td colspan="5" class="text-center">没有找到任何用户。</td>
      </tr>
    </c:if>
    <c:forEach var="user" items="${userList}">
      <tr>
        <td>${user.id}</td>
        <td><c:out value="${user.username}"/></td>
        <td><c:out value="${user.fullName}"/></td>
        <td><c:out value="${user.email}"/></td>
        <td>
          <a href="${pageContext.request.contextPath}/admin/manageUserRoles?userId=${user.id}" class="btn btn-primary btn-sm">
            管理角色
          </a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>