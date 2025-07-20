<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/19
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>创建新任务 - 智能实验室管理平台</title>
  <style>input[type="text"][placeholder] {
    color: #999;
  }
  </style>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <h3>创建新任务</h3>
  <p>请填写以下信息以创建一个新的实验任务。</p>

  <%-- 表单提交到 TaskServlet，并附带 action=insert 参数 --%>
  <form action="tasks?action=insert" method="post">
    <div class="form-group">
      <label for="taskName">任务名称</label>
      <input type="text" class="form-control" id="taskName" name="taskName" required>
    </div>

    <%--[cite_start] 这个字段用于关联具体项目，支持“实验项目全周期跟踪” [cite: 15] --%>
    <div class="form-group">
      <label for="projectId">关联项目ID</label>
      <input type="number" class="form-control" id="projectId" name="projectId" required placeholder="例如: 101">
    </div>

    <div class="form-group">
      <label for="taskDescription">详细描述</label>
      <textarea class="form-control" id="taskDescription" name="taskDescription" rows="4"></textarea>
    </div>

    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="dueDate">截止日期</label>
        <input type="text" class="form-control" id="dueDate" name="dueDate"
               placeholder="yyyy/mm/dd"
               onfocus="(this.type='date')"
               onblur="(this.type='text')">
      </div>
      <div class="form-group col-md-6">
        <label for="priority">优先级</label>
        <select id="priority" name="priority" class="form-control">
          <option>低</option>
          <option selected>中</option>
          <option>高</option>
        </select>
      </div>
    </div>

<%--    [cite_start]&lt;%&ndash; 这个字段用于指定任务负责人，支持“人员权限管理”中的任务分配 [cite: 6, 7] &ndash;%&gt;--%>
    <div class="form-group">
      <label for="userId">分配给 (用户ID)</label>
      <input type="number" class="form-control" id="userId" name="userId" required placeholder="例如: 1">
      <small class="form-text text-muted">在一个真实系统中，这里应该是一个用户选择下拉框。</small>
    </div>

    <button type="submit" class="btn btn-primary">保存任务</button>
    <a href="tasks" class="btn btn-secondary">取消</a>
  </form>
</div>
</body>
</html>