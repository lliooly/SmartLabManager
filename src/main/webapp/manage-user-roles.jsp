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
  <title>管理用户角色</title>
</head>
<body>
<div class="container">
  <h3>管理用户: <c:out value="${targetUser.fullName}" /> 的角色</h3>
  <form action="${pageContext.request.contextPath}/admin/manageUserRoles" method="post">
    <%-- 使用隐藏域传递要修改的用户ID --%>
    <input type="hidden" name="userId" value="${targetUser.id}">

    <div class="form-group">
      <label>请为用户分配角色:</label><br>
      <c:forEach var="role" items="${allRoles}">
        <div class="form-check">
          <input class="form-check-input" type="checkbox" name="roleIds" value="${role.id}"
            <%-- JSTL核心逻辑：如果用户已拥有该角色，则默认勾选 --%>
                 <c:if test="${currentUserRoleIds.contains(role.id)}">checked</c:if>
          >
          <label class="form-check-label">
            <c:out value="${role.roleName}" />
          </label>
        </div>
      </c:forEach>
    </div>

    <button type="submit" class="btn btn-success">保存更改</button>
    <a href="${pageContext.request.contextPath}/admin/userList" class="btn btn-secondary">返回</a>
  </form>
</div>
</body>
</html>