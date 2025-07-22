<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="我的申领"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-card-checklist"></i> 我的物资申领记录</h3>
    <a href="supply-request?action=show_form" class="btn btn-primary"><i class="bi bi-plus-circle"></i> 发起新申领</a>
  </div>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-hover">
        <thead><tr><th>物资名称</th><th>申领数量</th><th>申领日期</th><th>状态</th><th>备注</th></tr></thead>
        <tbody>
        <c:forEach var="req" items="${myRequests}">
          <tr>
            <td><c:out value="${req.supplyName}"/></td>
            <td>${req.quantityRequested}</td>
            <td><fmt:formatDate value="${req.requestDate}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><c:out value="${req.status}"/></td>
            <td><c:out value="${req.notes}"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty myRequests}"><td colspan="5" class="text-center">暂无申领记录</td></c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />