<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="场地详情"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-door-open"></i> 场地详情</h3>
    <c:url var="fallbackUrl" value="/venues"/>
    <a href="${not empty returnUrl ? returnUrl : fallbackUrl}" class="btn btn-outline-secondary"><i class="bi bi-arrow-left"></i> 返回</a>
  </div>

  <div class="row">
    <div class="col-lg-5">
      <div class="card card-glass mb-4">
        <div class="card-header"><h4><i class="bi bi-info-circle"></i> 场地信息</h4></div>
        <div class="card-body">
          <h4><c:out value="${venue.name}"/></h4>
          <p class="mb-1"><strong>位置:</strong> <c:out value="${venue.location}"/></p>
          <p class="mb-1"><strong>容量:</strong> ${venue.capacity} 人</p>
          <p class="mb-1"><strong>状态:</strong> <c:out value="${venue.status}"/></p>
          <p class="mb-0"><strong>描述:</strong> <c:out value="${venue.description}"/></p>
        </div>
      </div>
      <c:if test="${sessionScope.user.hasPermission('venue:book')}">
        <div class="card card-glass">
          <div class="card-header"><h4><i class="bi bi-calendar2-plus"></i> 发起新预约</h4></div>
          <div class="card-body">
            <form action="venues?action=book" method="post">
              <input type="hidden" name="venueId" value="${venue.id}">
              <div class="form-group"><label>开始时间:</label><input type="datetime-local" name="startTime" class="form-control" required></div>
              <div class="form-group"><label>结束时间:</label><input type="datetime-local" name="endTime" class="form-control" required></div>
              <div class="form-group"><label>用途说明:</label><textarea name="purpose" class="form-control" rows="2" required></textarea></div>
              <button type="submit" class="btn btn-primary">提交预约</button>
            </form>
          </div>
        </div>
      </c:if>
    </div>
    <div class="col-lg-7">
      <div class="card card-glass">
        <div class="card-header"><h4><i class="bi bi-calendar-week"></i> 当前预约情况</h4></div>
        <div class="card-body">
          <table class="table table-sm table-hover">
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
            <c:if test="${empty bookingList}"><td colspan="4" class="text-center text-muted">暂无预约</td></c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />