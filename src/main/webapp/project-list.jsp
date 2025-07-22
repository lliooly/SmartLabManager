<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="项目管理"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-folder2-open"></i> 实验项目列表</h3>
    <c:if test="${sessionScope.user.hasPermission('project:manage')}">
      <a href="${pageContext.request.contextPath}/projects?action=add_form" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 创建新项目
      </a>
    </c:if>
  </div>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>项目名称</th>
          <th>状态</th>
          <th>创建人</th>
          <th>开始日期</th>
          <th>结束日期</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="project" items="${projectList}">
          <tr>
            <td><strong><c:out value="${project.projectName}"/></strong></td>
            <td><span class="badge badge-info">${project.status}</span></td>
            <td><c:out value="${project.creatorName}"/></td>
            <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd"/></td>
            <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd"/></td>
            <td>
              <a href="${pageContext.request.contextPath}/projects?action=view&id=${project.id}&returnUrl=${pageContext.request.requestURI}" class="btn btn-info btn-sm">查看详情</a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />