<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="管理用户角色"/>
</jsp:include>

<div class="container main-content">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card card-glass">
        <div class="card-header">
          <h3><i class="bi bi-person-badge"></i> 管理用户: <span class="text-primary"><c:out value="${targetUser.fullName}" /></span> 的角色</h3>
        </div>
        <div class="card-body">
          <form action="${pageContext.request.contextPath}/admin/manageUserRoles" method="post">
            <input type="hidden" name="userId" value="${targetUser.id}">
            <div class="form-group">
              <label>请为用户分配角色:</label><br>
              <c:forEach var="role" items="${allRoles}">
                <div class="form-check my-2">
                  <input class="form-check-input" type="checkbox" name="roleIds" value="${role.id}" id="role-${role.id}"
                         <c:if test="${currentUserRoleIds.contains(role.id)}">checked</c:if>>
                  <label class="form-check-label" for="role-${role.id}">
                    <strong><c:out value="${role.roleName}" /></strong> - <small><c:out value="${role.description}"/></small>
                  </label>
                </div>
              </c:forEach>
            </div>
            <hr>
            <button type="submit" class="btn btn-success"><i class="bi bi-check-lg"></i> 保存更改</button>
            <a href="${pageContext.request.contextPath}/admin/userList" class="btn btn-secondary">返回</a>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />