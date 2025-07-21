<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <title>场地详情 - ${venue.name}</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
  <a href="${pageContext.request.contextPath}/venues" class="btn btn-secondary mb-3">返回场地列表</a>

  <c:if test="${param.error == 'conflict'}"><div class="alert alert-danger">预约失败：您选择的时间段与现有预约冲突。</div></c:if>
  <c:if test="${param.error == 'time_error'}"><div class="alert alert-danger">预约失败：结束时间不能早于开始时间。</div></c:if>
  <c:if test="${param.success == 'booked'}"><div class="alert alert-success">预约成功！</div></c:if>

  <div class="row">
    <div class="col-md-5">
      <div class="card mb-4">
        <div class="card-header"><h3>场地详情: <c:out value="${venue.name}"/></h3></div>
        <div class="card-body">
          <p><strong>位置:</strong> <c:out value="${venue.location}"/></p>
          <p><strong>容量:</strong> ${venue.capacity} 人</p>
          <p><strong>状态:</strong> <c:out value="${venue.status}"/></p>
          <p><strong>描述:</strong> <c:out value="${venue.description}"/></p>
        </div>
      </div>

      <c:if test="${sessionScope.user.hasPermission('venue:book')}">
        <h4>发起新预约</h4>
        <form action="venues?action=book" method="post">
          <input type="hidden" name="venueId" value="${venue.id}">
          <div class="form-group">
            <label>开始时间:</label>
            <input type="datetime-local" name="startTime" class="form-control" required>
          </div>
          <div class="form-group">
            <label>结束时间:</label>
            <input type="datetime-local" name="endTime" class="form-control" required>
          </div>
          <div class="form-group">
            <label>用途说明:</label>
            <textarea name="purpose" class="form-control" rows="2" required></textarea>
          </div>
          <button type="submit" class="btn btn-primary">提交预约</button>
        </form>
      </c:if>
    </div>
    <div class="col-md-7">
      <h4>当前预约情况</h4>
      <table class="table table-sm table-striped">
        <thead><tr><th>预约人</th><th>开始时间</th><th>结束时间</th><th>用途</th></tr></thead>
        <tbody>
        <c:forEach var="booking" items="${bookingList}">
          <tr>
            <td><c:out value="${booking.userName}"/></td>
            <td><fmt:formatDate value="${booking.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatDate value="${booking.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><c:out value="${booking.purpose}"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty bookingList}"><td colspan="4" class="text-center">暂无预约</td></c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>
