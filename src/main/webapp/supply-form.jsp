<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="添加新物资"/>
</jsp:include>

<div class="container main-content">
  <div class="row justify-content-center">
    <div class="col-lg-8">
      <div class="card card-glass">
        <div class="card-header"><h3><i class="bi bi-plus-circle"></i> 添加新物资</h3></div>
        <div class="card-body">
          <form action="supplies?action=insert" method="post">
            <div class="form-group"><label for="name">物资名称</label><input type="text" class="form-control" id="name" name="name" required></div>
            <div class="form-group"><label for="description">描述</label><textarea class="form-control" id="description" name="description" rows="3"></textarea></div>
            <div class="form-row">
              <div class="form-group col-md-4"><label for="quantityOnHand">初始库存数量</label><input type="number" class="form-control" id="quantityOnHand" name="quantityOnHand" value="0" required></div>
              <div class="form-group col-md-4"><label for="reorderLevel">库存预警阈值</label><input type="number" class="form-control" id="reorderLevel" name="reorderLevel" value="10" required></div>
              <div class="form-group col-md-4"><label for="unit">单位</label><input type="text" class="form-control" id="unit" name="unit" placeholder="例如: ml, g, 个"></div>
            </div>
            <button type="submit" class="btn btn-primary">保存物资</button>
            <a href="${pageContext.request.contextPath}/supplies" class="btn btn-secondary">取消</a>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/layout/footer.jsp" />