<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="物资库存"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-box-seam"></i> 物资库存列表</h3>
    <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
      <a href="${pageContext.request.contextPath}/supplies?action=add_form" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> 添加新物资
      </a>
    </c:if>
  </div>
  <p class="text-muted">库存数量低于预警阈值的物资将会被高亮显示。</p>

  <div class="card card-glass">
    <div class="card-body">
      <table class="table table-hover">
        <thead>
        <tr>
          <th>ID</th>
          <th>物资名称</th>
          <th>当前库存 (单位)</th>
          <th>预警阈值</th>
          <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
            <th>更新库存</th>
          </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="supply" items="${supplyList}">
          <tr class="${supply.quantityOnHand <= supply.reorderLevel ? 'table-danger' : ''}">
            <td>${supply.id}</td>
            <td><strong><c:out value="${supply.name}"/></strong><br><small class="text-muted"><c:out value="${supply.description}"/></small></td>
            <td>${supply.quantityOnHand} <c:out value="${supply.unit}"/></td>
            <td>${supply.reorderLevel}</td>
            <c:if test="${sessionScope.user.hasPermission('supply:manage')}">
              <td>
                <form action="supplies" method="post" class="form-inline">
                  <input type="hidden" name="action" value="update_quantity">
                  <input type="hidden" name="supplyId" value="${supply.id}">
                  <div class="form-group">
                    <input type="number" name="newQuantity" class="form-control form-control-sm" style="width: 80px;" value="${supply.quantityOnHand}" required>
                  </div>
                  <button type="submit" class="btn btn-sm btn-success ml-2">保存</button>
                </form>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />