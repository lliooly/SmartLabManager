<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="用户管理"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-people-fill"></i> 用户管理</h3>
    <div>
      <a href="${pageContext.request.contextPath}/admin/roleList" class="btn btn-info">角色与权限管理</a>
      <c:if test="${sessionScope.user.hasPermission('log:view')}">
        <a href="${pageContext.request.contextPath}/admin/audit-log" class="btn btn-secondary">查看审计日志</a>
      </c:if>
    </div>
  </div>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-hover">
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
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />