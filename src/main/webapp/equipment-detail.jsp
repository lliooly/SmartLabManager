<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
    <jsp:param name="pageTitle" value="设备详情"/>
</jsp:include>

<div class="container main-content">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3><i class="bi bi-cpu"></i> 设备详情</h3>
        <c:url var="fallbackUrl" value="/equipment"/>
        <a href="${not empty returnUrl ? returnUrl : fallbackUrl}" class="btn btn-outline-secondary mb-3">
            <i class="bi bi-arrow-left"></i> 返回
        </a>
    </div>

    <div class="row">
        <div class="col-lg-5">
            <div class="card card-glass mb-4">
                <div class="card-header"><h4><i class="bi bi-info-circle"></i> 设备信息</h4></div>
                <div class="card-body">
                    <h4><c:out value="${equipment.name}"/></h4>
                    <p class="mb-1"><strong>型号:</strong> <c:out value="${equipment.model}"/></p>
                    <p class="mb-1"><strong>序列号:</strong> <c:out value="${equipment.serialNumber}"/></p>
                    <p class="mb-1"><strong>状态:</strong> <c:out value="${equipment.status}"/></p>
                    <p class="mb-0"><strong>存放位置:</strong> <c:out value="${equipment.location}"/></p>
                </div>
            </div>
            <c:if test="${sessionScope.user.hasPermission('equipment:book')}">
                <div class="card card-glass">
                    <div class="card-header"><h4><i class="bi bi-calendar2-plus"></i> 发起新预约</h4></div>
                    <div class="card-body">
                        <form action="equipment?action=book" method="post">
                            <input type="hidden" name="equipmentId" value="${equipment.id}">
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
                        <thead><tr><th>预约人</th><th>开始时间</th><th>结束时间</th><th>操作</th></tr></thead>
                        <tbody>
                        <c:forEach var="booking" items="${bookingList}">
                            <tr>
                                <td><c:out value="${booking.userName}"/></td>
                                <td><fmt:formatDate value="${booking.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><fmt:formatDate value="${booking.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>
                                    <c:if test="${sessionScope.user.hasPermission('booking:manage')}">
                                        <form action="manage-booking" method="post" class="p-0 m-0 d-inline">
                                            <input type="hidden" name="bookingId" value="${booking.id}">
                                            <c:if test="${booking.status == '已预约'}"><button type="submit" name="action" value="borrow" class="btn btn-success btn-sm">借出</button></c:if>
                                            <c:if test="${booking.status == '使用中'}"><button type="submit" name="action" value="return" class="btn btn-warning btn-sm">归还</button></c:if>
                                        </form>
                                    </c:if>
                                    <c:if test="${sessionScope.user.id == booking.userId and booking.status == '已预约'}">
                                        <form action="manage-booking" method="post" class="p-0 m-0 d-inline" onsubmit="return confirm('您确定要取消这个预约吗？');">
                                            <input type="hidden" name="bookingId" value="${booking.id}">
                                            <button type="submit" name="action" value="cancel" class="btn btn-danger btn-sm">取消</button>
                                        </form>
                                    </c:if>
                                </td>
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