<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>审批物资申领 - 管理后台</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>待审批的物资申领</h3>
    <table class="table table-bordered">
        <thead class="thead-light">
        <tr>
            <th>申领人</th>
            <th>物资名称</th>
            <th>数量</th>
            <th>申领日期</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="req" items="${pendingRequests}">
            <tr>
                <td><c:out value="${req.requesterName}"/></td>
                <td><c:out value="${req.supplyName}"/></td>
                <td>${req.quantityRequested}</td>
                <td><fmt:formatDate value="${req.requestDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td><c:out value="${req.notes}"/></td>
                <td>
                    <form action="manage-requests" method="post" style="display: inline-block;">
                        <input type="hidden" name="requestId" value="${req.id}">
                        <button type="submit" name="status" value="已批准" class="btn btn-sm btn-success">批准</button>
                    </form>
                    <form action="manage-requests" method="post" style="display: inline-block;">
                        <input type="hidden" name="requestId" value="${req.id}">
                        <button type="submit" name="status" value="已驳回" class="btn btn-sm btn-danger">驳回</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty pendingRequests}"><td colspan="6" class="text-center">暂无待审批的申领</td></c:if>
        </tbody>
    </table>
</div>
</body>
</html>
