<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="角色与权限管理"/>
</jsp:include>

<div class="container main-content">
  <h3><i class="bi bi-shield-lock-fill"></i> 角色与权限管理</h3>

  <div class="card card-glass">
    <div class="card-body">
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
              <a href="${pageContext.request.contextPath}/admin/manageRolePermissions?roleId=${role.id}&returnUrl=${pageContext.request.contextPath}/admin/roleList" class="btn btn-info btn-sm">
                管理权限
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />