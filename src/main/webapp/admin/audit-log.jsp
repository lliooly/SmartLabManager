<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="审计日志"/>
</jsp:include>

<div class="container main-content">
  <h3><i class="bi bi-shield-check"></i> 审计日志查询</h3>

  <div class="card card-glass mb-4">
    <div class="card-body">
      <form class="form-inline" action="audit-log" method="get">
        <div class="form-group mr-3">
          <label for="username" class="mr-2">操作员:</label>
          <input type="text" name="username" id="username" class="form-control" placeholder="用户名" value="${param.username}">
        </div>
        <div class="form-group mr-3">
          <label for="startDate" class="mr-2">开始日期:</label>
          <input type="date" name="startDate" id="startDate" class="form-control" value="${param.startDate}">
        </div>
        <div class="form-group mr-3">
          <label for="endDate" class="mr-2">结束日期:</label>
          <input type="date" name="endDate" id="endDate" class="form-control" value="${param.endDate}">
        </div>
        <button type="submit" class="btn btn-primary"><i class="bi bi-search"></i> 查询</button>
        <a href="audit-log" class="btn btn-secondary ml-2">清空</a>
      </form>
    </div>
  </div>

  <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-bordered table-sm table-striped">
        <thead class="thead-dark">
        <tr>
          <th>时间</th>
          <th>操作员</th>
          <th>IP地址</th>
          <th>操作内容</th>
          <th>目标</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${logs}">
          <tr>
            <td><fmt:formatDate value="${log.actionTimestamp}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:out value="${log.operatorUsername}"/> (ID:${log.operatorId})</td>
            <td><c:out value="${log.ipAddress}"/></td>
            <td><c:out value="${log.action}"/></td>
            <td><c:out value="${log.targetType}"/> (ID:${log.targetId})</td>
          </tr>
        </c:forEach>
        <c:if test="${empty logs}">
          <tr><td colspan="5" class="text-center">没有找到符合条件的日志记录。</td></tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />