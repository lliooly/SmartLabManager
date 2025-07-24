<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="创建新项目"/>
</jsp:include>

<div class="container main-content">
  <div class="row justify-content-center">
    <div class="col-lg-10">
      <div class="card card-glass">
        <div class="card-header"><h3><i class="bi bi-plus-circle"></i> 创建新实验项目</h3></div>
        <div class="card-body">
          <p class="text-muted">请尽可能详细地填写项目信息，这将有助于后续的AI风险评估。</p>
          <hr>
          <form action="projects?action=insert" method="post">
            <div class="form-group"><label><strong>项目名称</strong></label><input type="text" class="form-control" name="projectName" required></div>
            <div class="form-group"><label><strong>项目简述</strong></label><textarea class="form-control" name="description" rows="3"></textarea></div>
            <div class="form-row">
              <div class="form-group col-md-6"><label><strong>计划开始日期</strong></label><input type="text" class="form-control" id="startDate" name="startDate" placeholder="请选择开始日期..."></div>
              <div class="form-group col-md-6"><label><strong>计划结束日期</strong></label><input type="text" class="form-control" id="endDate" name="endDate" placeholder="请选择结束日期..."></div>
            </div>
            <div class="form-group"><label><strong>实验目的</strong></label><textarea class="form-control" name="purpose" rows="4"></textarea></div>
            <div class="form-group"><label><strong>详细操作步骤</strong></label><textarea class="form-control" name="procedureSteps" rows="8" placeholder="请分点列出详细的实验操作流程..."></textarea></div>
            <div class="form-group"><label><strong>所用试剂和设备</strong></label><textarea class="form-control" name="reagentsAndEquipment" rows="4" placeholder="请列出所需的主要试剂、耗材和设备..."></textarea></div>
            <button type="submit" class="btn btn-primary">提交立项申请</button>
            <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">取消</a>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/WEB-INF/layout/footer.jsp" />