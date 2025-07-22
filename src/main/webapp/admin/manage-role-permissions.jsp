<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="管理角色权限"/>
</jsp:include>

<div class="container main-content">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card card-glass">
        <div class="card-header">
          <h3><i class="bi bi-shield-lock-fill"></i> 管理角色: <span class="text-primary"><c:out value="${targetRole.roleName}"/></span> 的权限</h3>
        </div>
        <div class="card-body">
          <form action="${pageContext.request.contextPath}/admin/manageRolePermissions" method="post">
            <input type="hidden" name="roleId" value="${targetRole.id}">
            <div class="form-group">
              <label>请为该角色勾选权限:</label>
              <c:forEach var="permission" items="${allPermissions}">
                <div class="form-check my-2">
                  <input class="form-check-input" type="checkbox" name="permissionIds" value="${permission.id}" id="perm-${permission.id}"
                         <c:if test="${currentPermissionIds.contains(permission.id)}">checked</c:if>>
                  <label class="form-check-label" for="perm-${permission.id}">
                    <strong><c:out value="${permission.permissionName}"/></strong>:
                    <small class="text-muted"><c:out value="${permission.description}"/></small>
                  </label>
                </div>
              </c:forEach>
            </div>
            <hr>
            <button type="submit" class="btn btn-success"><i class="bi bi-check-lg"></i> 保存更改</button>
            <c:url var="fallbackUrl" value="/admin/role-list.jsp"/>
            <a href="${not empty returnUrl ? returnUrl : fallbackUrl}" class="btn btn-secondary">返回</a>          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />