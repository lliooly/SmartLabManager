<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/20
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>管理角色权限</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>管理角色: <span class="text-primary"><c:out value="${targetRole.roleName}"/></span> 的权限</h3>
  <form action="${pageContext.request.contextPath}/admin/manageRolePermissions" method="post">
    <input type="hidden" name="roleId" value="${targetRole.id}">
    <div class="form-group">
      <label>请为该角色勾选权限:</label>
      <div class="card p-3">
        <c:forEach var="permission" items="${allPermissions}">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" name="permissionIds" value="${permission.id}"
                   <c:if test="${currentPermissionIds.contains(permission.id)}">checked</c:if>
            >
            <label class="form-check-label">
              <strong><c:out value="${permission.permissionName}"/></strong>:
              <small class="text-muted"><c:out value="${permission.description}"/></small>
            </label>
          </div>
        </c:forEach>
      </div>
    </div>
    <button type="submit" class="btn btn-success">保存更改</button>
    <a href="${pageContext.request.contextPath}/admin/roleList" class="btn btn-secondary">返回</a>
  </form>
</div>
</body>
</html>