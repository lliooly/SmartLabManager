<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <title>我的申领</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>我的物资申领记录</h3>
  <a href="supply-request?action=show_form" class="btn btn-primary mb-3">发起新申领</a>
  <table class="table table-bordered">
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
    </tbody>
  </table>
</div>
</body>
</html>