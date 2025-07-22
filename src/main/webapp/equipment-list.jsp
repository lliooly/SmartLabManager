<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="设备管理"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-cpu"></i> 设备列表</h3>
    <c:if test="${sessionScope.user.hasPermission('equipment:create')}">
      <a href="${pageContext.request.contextPath}/equipment?action=add_form" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 添加新设备
      </a>
    </c:if>
  </div>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>ID</th>
          <th>设备名称</th>
          <th>型号</th>
          <th>状态</th>
          <th>存放位置</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="equ" items="${equipmentList}">
          <tr>
            <td>${equ.id}</td>
            <td><strong><c:out value="${equ.name}"/></strong></td>
            <td><c:out value="${equ.model}"/></td>
            <td>
              <c:choose>
                <c:when test="${equ.status == '可用'}"><span class="badge badge-success">${equ.status}</span></c:when>
                <c:when test="${equ.status == '使用中'}"><span class="badge badge-warning">${equ.status}</span></c:when>
                <c:when test="${equ.status == '维修中'}"><span class="badge badge-secondary">${equ.status}</span></c:when>
                <c:when test="${equ.status == '已报废'}"><span class="badge badge-danger">${equ.status}</span></c:when>
                <c:otherwise><span class="badge badge-light">${equ.status}</span></c:otherwise>
              </c:choose>
            </td>
            <td><c:out value="${equ.location}"/></td>
            <td>
              <a href="${pageContext.request.contextPath}/equipment?action=view&id=${equ.id}" class="btn btn-info btn-sm">查看详情 & 预约</a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />