<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
    <jsp:param name="pageTitle" value="场地管理"/>
</jsp:include>

<div class="container main-content">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3><i class="bi bi-door-open"></i> 场地列表</h3>
        <c:if test="${sessionScope.user.hasPermission('venue:manage')}">
            <a href="${pageContext.request.contextPath}/venues?action=add_form" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> 添加新场地
            </a>
        </c:if>
    </div>

    <div class="card card-glass">
        <div class="card-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>场地名称</th>
                    <th>位置</th>
                    <th>容量</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="venue" items="${venueList}">
                    <tr>
                        <td><strong><c:out value="${venue.name}"/></strong></td>
                        <td><c:out value="${venue.location}"/></td>
                        <td>${venue.capacity} 人</td>
                        <td><span class="badge badge-success">${venue.status}</span></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/venues?action=view&id=${venue.id}" class="btn btn-info btn-sm">查看详情 & 预约</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />