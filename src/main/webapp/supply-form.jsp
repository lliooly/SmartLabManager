<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>添加新物资 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>添加新物资</h3>
  <p>请填写以下信息以录入一个新的实验耗材。</p>
  <hr>

  <form action="supplies?action=insert" method="post">
    <div class="form-group">
      <label for="name">物资名称</label>
      <input type="text" class="form-control" id="name" name="name" required>
    </div>
    <div class="form-group">
      <label for="description">描述</label>
      <textarea class="form-control" id="description" name="description" rows="3"></textarea>
    </div>
    <div class="form-row">
      <div class="form-group col-md-4">
        <label for="quantityOnHand">初始库存数量</label>
        <input type="number" class="form-control" id="quantityOnHand" name="quantityOnHand" value="0" required>
      </div>
      <div class="form-group col-md-4">
        <label for="reorderLevel">库存预警阈值</label>
        <input type="number" class="form-control" id="reorderLevel" name="reorderLevel" value="10" required>
      </div>
      <div class="form-group col-md-4">
        <label for="unit">单位</label>
        <input type="text" class="form-control" id="unit" name="unit" placeholder="例如: ml, g, 个">
      </div>
    </div>

    <button type="submit" class="btn btn-primary">保存物资</button>
    <a href="${pageContext.request.contextPath}/supplies" class="btn btn-secondary">取消</a>
  </form>
</div>
</body>
</html>
