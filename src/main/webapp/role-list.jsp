<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/20
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>角色与权限管理</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>角色与权限管理</h3>
  <table class="table table-hover">
    <thead>
    <tr>
      <th>角色ID</th>
      <th>角色名称</th>
      <th>描述</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="role" items="${allRoles}">
      <tr>
        <td>${role.id}</td>
        <td><c:out value="${role.roleName}"/></td>
        <td><c:out value="${role.description}"/></td>
        <td>
          <a href="${pageContext.request.contextPath}/admin/manageRolePermissions?roleId=${role.id}" class="btn btn-info btn-sm">
            管理权限
          </a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <a href="${pageContext.request.contextPath}/admin/userList" class="btn btn-secondary">返回用户管理</a>
</div>
</body>
</html>