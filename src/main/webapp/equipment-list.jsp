<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>设备列表 - 智能实验室管理平台</title>

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">

  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3>设备列表</h3>

    <c:if test="${sessionScope.user.hasPermission('equipment:create')}">
      <a href="${pageContext.request.contextPath}/equipment?action=add_form" class="btn btn-primary">添加新设备</a>
    </c:if>
  </div>

  <table class="table table-hover table-bordered">
    <thead class="thead-light">
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
    <%-- 检查列表是否为空 --%>
    <c:if test="${empty equipmentList}">
      <tr>
        <td colspan="6" class="text-center text-muted">当前没有任何设备信息。</td>
      </tr>
    </c:if>

    <%-- 遍历由Servlet传过来的设备列表 --%>
    <c:forEach var="equ" items="${equipmentList}">
      <tr>
        <td>${equ.id}</td>
        <td><c:out value="${equ.name}"/></td>
        <td><c:out value="${equ.model}"/></td>

        <td>
          <c:choose>
            <c:when test="${equ.status == '可用'}">
              <span class="badge badge-success">${equ.status}</span>
            </c:when>
            <c:when test="${equ.status == '使用中'}">
              <span class="badge badge-warning">${equ.status}</span>
            </c:when>
            <c:when test="${equ.status == '维修中'}">
              <span class="badge badge-secondary">${equ.status}</span>
            </c:when>
            <c:when test="${equ.status == '已报废'}">
              <span class="badge badge-danger">${equ.status}</span>
            </c:when>
            <c:otherwise>
              <span class="badge badge-light">${equ.status}</span>
            </c:otherwise>
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

  <a href="#" class="btn btn-link mt-3">返回仪表盘</a>

</div>
</body>
</html>