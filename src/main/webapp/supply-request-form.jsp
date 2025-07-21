<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>申领物资</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>发起物资申领</h3>
  <form action="supply-request" method="post">
    <div class.form-group">
    <label for="supplyId">选择物资</label>
    <select name="supplyId" id="supplyId" class="form-control" required>
      <option value="">--请选择--</option>
      <c:forEach var="supply" items="${allSupplies}">
        <option value="${supply.id}"><c:out value="${supply.name}"/> (当前库存: ${supply.quantityOnHand})</option>
      </c:forEach>
    </select>
</div>
<div class="form-group">
  <label for="quantity">申领数量</label>
  <input type="number" name="quantity" id="quantity" class="form-control" required min="1">
</div>
<div class="form-group">
  <label for="notes">备注说明</label>
  <textarea name="notes" id="notes" class="form-control" rows="3"></textarea>
</div>
<button type="submit" class="btn btn-primary">提交申领</button>
<a href="supply-request?action=my_list" class="btn btn-secondary">返回我的申领列表</a>
</form>
</div>
</body>
</html>