<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>物资库存 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3>物资库存列表</h3>
    <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
      <a href="${pageContext.request.contextPath}/venues?action=add_form" class="btn btn-primary">添加新场地</a>
    </c:if>
  </div>
  <p class="text-muted">库存数量低于预警阈值的物资将会被高亮显示。</p>

  <table class="table table-hover table-bordered">
    <thead class="thead-light">
    <tr>
      <th>ID</th>
      <th>物资名称</th>
      <th>当前库存 (单位)</th>
      <th>预警阈值</th>
      <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
        <th>更新库存</th>
      </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="supply" items="${supplyList}">
      <tr class="${supply.quantityOnHand <= supply.reorderLevel ? 'table-danger' : ''}">
        <td>${supply.id}</td>
        <td><c:out value="${supply.name}"/> <br> <small class="text-muted"><c:out value="${supply.description}"/></small></td>
        <td>${supply.quantityOnHand} <c:out value="${supply.unit}"/></td>
        <td>${supply.reorderLevel}</td>

          <%-- 权限控制的操作列 --%>
        <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
          <td>
              <%-- 每个物资一行一个表单，用于更新库存 --%>
            <form action="supplies" method="post" class="form-inline">
              <input type="hidden" name="action" value="update_quantity">
              <input type="hidden" name="supplyId" value="${supply.id}">
              <div class="form-group">
                <input type="number" name="newQuantity" class="form-control form-control-sm" style="width: 80px;" value="${supply.quantityOnHand}" required>
              </div>
              <button type="submit" class="btn btn-sm btn-success ml-2">保存</button>
            </form>
          </td>
        </c:if>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
