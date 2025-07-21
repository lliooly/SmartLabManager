<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 08:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>添加新设备 - 智能实验室管理平台</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>添加新设备</h3>
    <p>请填写以下信息以录入一个新的实验设备。</p>
    <hr>

    <form action="equipment?action=insert" method="post">
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="name">设备名称</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="form-group col-md-6">
                <label for="model">型号</label>
                <input type="text" class="form-control" id="model" name="model">
            </div>
        </div>
        <div class="form-group">
            <label for="serialNumber">序列号</label>
            <input type="text" class="form-control" id="serialNumber" name="serialNumber" required>
        </div>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="location">存放位置</label>
                <input type="text" class="form-control" id="location" name="location">
            </div>
            <div class="form-group col-md-6">
                <label for="status">初始状态</label>
                <select id="status" name="status" class="form-control">
                    <option selected>可用</option>
                    <option>维修中</option>
                    <option>已报废</option>
                </select>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="purchaseDate">采购日期</label>
                <input type="date" class="form-control" id="purchaseDate" name="purchaseDate">
            </div>
            <div class="form-group col-md-6">
                <label for="lastMaintenanceDate">上次维保日期</label>
                <input type="date" class="form-control" id="lastMaintenanceDate" name="lastMaintenanceDate">
            </div>
        </div>

        <button type="submit" class="btn btn-primary">保存设备</button>
        <a href="${pageContext.request.contextPath}/equipment" class="btn btn-secondary">取消</a>
    </form>
</div>
</body>
</html>