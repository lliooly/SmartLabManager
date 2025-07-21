<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>场地列表 - 智能实验室管理平台</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>场地列表</h3>
        <c:if test="${sessionScope.user.hasPermission('venue:manage')}">
            <a href="${pageContext.request.contextPath}/venues?action=view&id=${venue.id}" class="btn btn-info btn-sm">查看详情 & 预约</a>
        </c:if>
    </div>
    <table class="table table-hover table-bordered">
        <thead class="thead-light">
        <tr>
            <th>场地名称</th>
            <th>位置</th>
            <th>容量</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="venue" items="${venueList}">
            <tr>
                <td><c:out value="${venue.name}"/></td>
                <td><c:out value="${venue.location}"/></td>
                <td>${venue.capacity} 人</td>
                <td><span class="badge badge-success">${venue.status}</span></td>
                <td>
                    <a href="#" class="btn btn-info btn-sm">查看详情 & 预约</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
