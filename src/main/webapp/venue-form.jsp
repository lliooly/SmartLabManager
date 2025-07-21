<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>添加新场地 - 智能实验室管理平台</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>添加新场地</h3>
    <hr>
    <form action="venues?action=insert" method="post">
        <div class="form-group">
            <label for="name">场地名称</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="description">描述</label>
            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
        </div>
        <div class="form-row">
            <div class="form-group col-md-4">
                <label for="location">位置</label>
                <input type="text" class="form-control" id="location" name="location">
            </div>
            <div class="form-group col-md-4">
                <label for="capacity">容量 (人)</label>
                <input type="number" class="form-control" id="capacity" name="capacity" value="10" min="0">
            </div>
            <div class="form-group col-md-4">
                <label for="status">初始状态</label>
                <select id="status" name="status" class="form-control">
                    <option selected>可用</option>
                    <option>维修中</option>
                    <option>已停用</option>
                </select>
            </div>
        </div>
        <button type="submit" class="btn btn-primary">保存场地</button>
        <a href="${pageContext.request.contextPath}/venues" class="btn btn-secondary">取消</a>
    </form>
</div>
</body>
</html>
