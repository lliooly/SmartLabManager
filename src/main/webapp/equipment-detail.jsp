<%--
  Created by IntelliJ IDEA.
  User: 16040
  Date: 2025/7/21
  Time: 08:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>设备详情 - ${equipment.name}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <a href="${pageContext.request.contextPath}/equipment" class="btn btn-secondary mb-3">返回设备列表</a>

    <c:if test="${param.error == 'conflict'}"><div class="alert alert-danger">预约失败：您选择的时间段与现有预约冲突。</div></c:if>
    <c:if test="${param.success == 'booked'}"><div class="alert alert-success">预约成功！</div></c:if>

    <div class="card mb-4">
        <div class="card-header">
            <h3>设备详情: <c:out value="${equipment.name}"/></h3>
        </div>
        <div class="card-body">
            <p><strong>型号:</strong> <c:out value="${equipment.model}"/></p>
            <p><strong>序列号:</strong> <c:out value="${equipment.serialNumber}"/></p>
            <p><strong>状态:</strong> <c:out value="${equipment.status}"/></p>
            <p><strong>存放位置:</strong> <c:out value="${equipment.location}"/></p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-7">
            <h4>当前预约情况</h4>
            <table class="table table-sm table-striped">
                <thead>
                <tr>
                    <th>预约人</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>用途</th>
                    <th>操作</th> <%-- 统一显示“操作”列 --%>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="booking" items="${bookingList}">
                    <tr>
                        <td><c:out value="${booking.userName}"/></td>
                        <td><fmt:formatDate value="${booking.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td><fmt:formatDate value="${booking.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td><c:out value="${booking.purpose}"/></td>
                        <td>
                                <%-- 管理员/教师的“借还”操作 --%>
                            <c:if test="${sessionScope.user.hasPermission('booking:manage')}">
                                <form action="manage-booking" method="post" style="display:inline;">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <c:if test="${booking.status == '已预约'}">
                                        <button type="submit" name="action" value="borrow" class="btn btn-success btn-sm">确认借出</button>
                                    </c:if>
                                    <c:if test="${booking.status == '使用中'}">
                                        <button type="submit" name="action" value="return" class="btn btn-warning btn-sm">确认归还</button>
                                    </c:if>
                                </form>
                            </c:if>

                                <%-- 用户取消自己的预约 --%>
                            <c:if test="${sessionScope.user.id == booking.userId and booking.status == '已预约'}">
                                <form action="manage-booking" method="post" style="display:inline;" onsubmit="return confirm('您确定要取消这个预约吗？');">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <button type="submit" name="action" value="cancel" class="btn btn-danger btn-sm">取消预约</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <%-- ... --%>
                </tbody>
            </table>
        </div>
        <div class="col-md-5">
            <c:if test="${sessionScope.user.hasPermission('equipment:book')}">
                <h4>发起新预约</h4>
                <form action="equipment?action=book" method="post">
                    <input type="hidden" name="equipmentId" value="${equipment.id}">
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
    </div>
</div>
</body>
</html>
