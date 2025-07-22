<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
    <jsp:param name="pageTitle" value="设备维保管理"/>
</jsp:include>

<div class="container main-content">
    <h3><i class="bi bi-wrench-adjustable"></i> 设备维保管理</h3>
    <p class="text-muted">在此页面集中更新所有设备的维保状态和日期。</p>

    <div class="card card-glass">
        <div class="card-body">
            <table class="table table-bordered">
                <thead class="thead-light">
                <tr>
                    <th>设备名称</th>
                    <th>当前状态</th>
                    <th>上次维保日期</th>
                    <th>更新维保记录</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="equ" items="${equipmentList}">
                    <tr>
                        <td><strong><c:out value="${equ.name}"/></strong><br><small class="text-muted">ID: ${equ.id}</small></td>
                        <td>${equ.status}</td>
                        <td><fmt:formatDate value="${equ.lastMaintenanceDate}" pattern="yyyy-MM-dd"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/maintenance" method="post">
                                <input type="hidden" name="equipmentId" value="${equ.id}">
                                <div class="form-group">
                                    <input type="date" class="form-control mb-2" name="lastMaintenanceDate" required>
                                </div>
                                <div class="form-group">
                                    <select class="form-control mb-2" name="status">
                                        <option value="可用">完成维保 (状态变为可用)</option>
                                        <option value="维修中">开始维保 (状态变为维修中)</option>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-sm btn-success">更新</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />