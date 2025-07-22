<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="申领物资"/>
</jsp:include>

<div class="container main-content">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card card-glass">
        <div class="card-header"><h3><i class="bi bi-box-seam"></i> 发起物资申领</h3></div>
        <div class="card-body">
          <form action="supply-request" method="post">
            <div class="form-group">
              <label for="supplyId">选择物资</label>
              <select name="supplyId" id="supplyId" class="form-control" required>
                <option value="">--请选择--</option>
                <c:forEach var="supply" items="${allSupplies}">
                  <option value="${supply.id}"><c:out value="${supply.name}"/> (当前库存: ${supply.quantityOnHand})</option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label for="quantity">申领数量</label>
              <input type="number" name="quantity" id="quantity" class="form-control" required min="1">
            </div>
            <div class="form-group">
              <label for="notes">备注说明</label>
              <textarea name="notes" id="notes" class="form-control" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">提交申领</button>
            <a href="supply-request?action=my_list" class="btn btn-secondary">返回我的申领列表</a>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />