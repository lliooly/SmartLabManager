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
  <title>管理用户角色 - 管理后台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>管理用户: <span class="text-primary"><c:out value="${targetUser.fullName}" /></span> 的角色</h3>
  <hr>
  <form action="${pageContext.request.contextPath}/admin/manageUserRoles" method="post">
    <input type="hidden" name="userId" value="${targetUser.id}">

    <div class="form-group">
      <label>请为用户分配角色:</label>
      <div class="card p-3">
        <c:forEach var="role" items="${allRoles}">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" name="roleIds" value="${role.id}" id="role-${role.id}"
                   <c:if test="${currentUserRoleIds.contains(role.id)}">checked</c:if>
            >
            <label class="form-check-label" for="role-${role.id}">
              <c:out value="${role.roleName}" />
            </label>
          </div>
        </c:forEach>
      </div>
    </div>

    <button type="submit" class="btn btn-success">保存更改</button>
    <a href="${pageContext.request.contextPath}/admin/userList" class="btn btn-secondary">返回</a>
  </form>
</div>
</body>
</html>