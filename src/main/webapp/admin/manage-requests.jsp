<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
    <jsp:param name="pageTitle" value="审批物资申领"/>
</jsp:include>

<div class="container main-content">
    <h3><i class="bi bi-check2-square"></i> 待审批的物资申领</h3>

    <div class="card card-glass">
        <div class="card-body">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr>
                    <th>申领人</th>
                    <th>物资名称</th>
                    <th>数量</th>
                    <th>申领日期</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="req" items="${pendingRequests}">
                    <tr>
                        <td><c:out value="${req.requesterName}"/></td>
                        <td><strong><c:out value="${req.supplyName}"/></strong></td>
                        <td>${req.quantityRequested}</td>
                        <td><fmt:formatDate value="${req.requestDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td><c:out value="${req.notes}"/></td>
                        <td>
                            <form action="manage-requests" method="post" class="d-inline-block">
                                <input type="hidden" name="requestId" value="${req.id}">
                                <button type="submit" name="status" value="已批准" class="btn btn-sm btn-success"><i class="bi bi-check-lg"></i> 批准</button>
                            </form>
                            <form action="manage-requests" method="post" class="d-inline-block">
                                <input type="hidden" name="requestId" value="${req.id}">
                                <button type="submit" name="status" value="已驳回" class="btn btn-sm btn-danger"><i class="bi bi-x-lg"></i> 驳回</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty pendingRequests}"><td colspan="6" class="text-center text-muted">暂无待审批的申领</td></c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />