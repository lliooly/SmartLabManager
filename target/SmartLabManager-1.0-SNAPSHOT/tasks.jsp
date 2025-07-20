<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/19
  Time: 16:24
  To change this template use File | Settings | File Templates.
<%-- 引入 JSTL 核心标签库，这是使用 <c:forEach> 等标签的前提 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>实验室任务面板 - 智能实验室管理平台</title>
  <%--[cite_start] 根据项目技术要求，我们使用 Bootstrap 来美化界面 [cite: 28] --%>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3>实验室任务面板</h3>
    <%-- 这个链接会指向 TaskServlet，并带上 action=new 参数，用于显示添加任务的表单 --%>
    <a href="tasks?action=new" class="btn btn-primary">创建新任务</a>
  </div>
  <p class="text-muted">这里显示了分配给您的所有任务，请及时跟踪进度。</p>

  <table class="table table-bordered table-hover">
    <thead class="thead-dark">
    <tr>
      <th>任务名称</th>
      <th>描述</th>
      <th>截止日期</th>
      <th>优先级</th>
      <th>状态</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%-- 使用 JSTL 的 forEach 标签遍历由 Servlet 传过来的 taskList --%>
    <c:forEach var="task" items="${taskList}">
      <tr>
          <%-- 使用 <c:out> 标签输出内容，可以防止 XSS 攻击 --%>
        <td><c:out value="${task.taskName}" /></td>
        <td><c:out value="${task.taskDescription}" /></td>
        <td><c:out value="${task.dueDate}" /></td>
        <td>
            <%-- 根据任务的优先级，显示不同颜色的徽章，更加直观 --%>
          <span class="badge
                            <c:if test='${task.priority == "High"}'>badge-danger</c:if>
                            <c:if test='${task.priority == "Medium"}'>badge-warning</c:if>
                            <c:if test='${task.priority == "Low"}'>badge-info</c:if>
                        ">
                            <c:out value="${task.priority}" />
                        </span>
        </td>
        <td><c:out value="${task.status}" /></td>
        <td>
            <%-- 未来可以添加编辑和删除按钮 --%>
          <a href="#" class="btn btn-sm btn-secondary">编辑</a>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <%-- 如果任务列表为空，显示提示信息 --%>
  <c:if test="${empty taskList}">
    <div class="alert alert-info text-center">当前没有待办任务。</div>
  </c:if>
</div>
</body>
</html>