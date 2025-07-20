<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/19
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>用户管理</title>
  <%-- 引入Bootstrap等样式 --%>
</head>
<body>
<div class="container">
  <h2>用户管理</h2>
  <table class="table">
    <thead>
    <tr>
      <th>ID</th>
      <th>用户名</th>
      <th>全名</th>
      <th>邮箱</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${userList}">
      <tr>
        <td>${user.id}</td>
        <td>${user.username}</td>
        <td>${user.fullName}</td>
        <td>${user.email}</td>
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