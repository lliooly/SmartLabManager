<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 09:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>设备维保管理 - 管理后台</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>设备维保管理</h3>
    <p class="text-muted">在此页面集中更新所有设备的维保状态和日期。</p>
    <table class="table table-bordered">
        <thead class="thead-light">
        <tr>
            <th>设备名称</th>
            <th>当前状态</th>
            <th>上次维保日期</th>
            <th>更新维保记录</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="equ" items="${equipmentList}">
            <tr>
                <td><c:out value="${equ.name}"/> <br><small class="text-muted">ID: ${equ.id}</small></td>
                <td>${equ.status}</td>
                <td><fmt:formatDate value="${equ.lastMaintenanceDate}" pattern="yyyy-MM-dd"/></td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/maintenance" method="post">
                        <input type="hidden" name="equipmentId" value="${equ.id}">
                        <div class="form-group">
                            <label for="maintDate-${equ.id}" class="sr-only">维保日期</label>
                            <input type="date" class="form-control mb-2" id="maintDate-${equ.id}" name="lastMaintenanceDate" required>
                        </div>
                        <div class="form-group">
                            <label for="status-${equ.id}" class="sr-only">新状态</label>
                            <select class="form-control mb-2" id="status-${equ.id}" name="status">
                                <option value="可用">完成维保 (状态变为可用)</option>
                                <option value="维修中">开始维保 (状态变为维修中)</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-sm btn-success">更新</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/admin/userList" class="btn btn-secondary mt-3">返回用户管理</a>
</div>
</body>
</html>