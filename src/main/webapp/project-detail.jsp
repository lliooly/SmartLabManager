<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="项目详情"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-folder2-open"></i> 项目详情</h3>
    <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary"><i class="bi bi-arrow-left"></i> 返回项目列表</a>
  </div>
  <c:if test="${param.assessment == 'done'}"><div class="alert alert-success">AI风险评估已完成并保存！</div></c:if>

  <div class="row">
    <div class="col-lg-8">
      <div class="card card-glass mb-4">
        <div class="card-header font-weight-bold"><i class="bi bi-list-task"></i> 项目任务</div>
        <div class="card-body">
          <table class="table table-sm table-hover">
            <thead class="thead-light">
            <tr>
              <th>任务名称</th>
              <th>负责人</th>
              <th>截止日期</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="task" items="${taskList}">
              <tr>
                <td><c:out value="${task.taskName}"/></td>
                <td><c:out value="${task.assigneeName}" default="未分配"/></td>
                <td><fmt:formatDate value="${task.dueDate}" pattern="yyyy-MM-dd"/></td>
                <td>
                  <c:if test="${sessionScope.user.hasPermission('task:edit')}">
                    <form action="task" method="post" class="form-inline p-0 m-0">
                      <input type="hidden" name="action" value="update_status">
                      <input type="hidden" name="taskId" value="${task.id}">
                      <input type="hidden" name="projectId" value="${project.id}">
                      <select name="status" class="form-control form-control-sm" onchange="this.form.submit()">
                        <option value="待办" ${task.status == '待办' ? 'selected' : ''}>待办</option>
                        <option value="进行中" ${task.status == '进行中' ? 'selected' : ''}>进行中</option>
                        <option value="已完成" ${task.status == '已完成' ? 'selected' : ''}>已完成</option>
                      </select>
                    </form>
                  </c:if>
                  <c:if test="${!sessionScope.user.hasPermission('task:edit')}">
                    <c:out value="${task.status}"/>
                  </c:if>
                </td>
                <td>
                  <c:if test="${sessionScope.user.hasPermission('task:delete')}">
                    <form action="task" method="post" class="p-0 m-0" onsubmit="return confirm('确定要删除这个任务吗？');">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="taskId" value="${task.id}">
                      <input type="hidden" name="projectId" value="${project.id}">
                      <button type="submit" class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty taskList}"><tr><td colspan="5" class="text-center text-muted">该项目下暂无任务</td></tr></c:if>
            </tbody>
          </table>
        </div>
      </div>
      <c:if test="${sessionScope.user.hasPermission('task:create')}">
        <div class="card card-glass">
          <div class="card-header font-weight-bold"><i class="bi bi-plus-circle"></i> 添加新任务</div>
          <div class="card-body">
            <form action="projects?action=create_task" method="post">
              <input type="hidden" name="projectId" value="${project.id}">
              <div class="form-group">
                <label for="taskName">任务名称</label>
                <input type="text" id="taskName" name="taskName" class="form-control" required>
              </div>
              <div class="form-group">
                <label for="description">任务描述</label>
                <textarea id="description" name="description" class="form-control" rows="2"></textarea>
              </div>
              <div class="form-row">
                <div class="form-group col-md-4">
                  <label for="assigneeId">分配给</label>
                  <select id="assigneeId" name="assigneeId" class="form-control">
                    <option value="0">-- 未分配 --</option>
                    <c:forEach var="user" items="${userList}">
                      <option value="${user.id}">${user.fullName}</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="form-group col-md-4">
                  <label for="dueDate">截止日期</label>
                  <input type="date" id="dueDate" name="dueDate" class="form-control">
                </div>
                <div class="form-group col-md-4">
                  <label for="priority">优先级</label>
                  <select id="priority" name="priority" class="form-control">
                    <option>低</option>
                    <option selected>中</option>
                    <option>高</option>
                  </select>
                </div>
              </div>
              <button type="submit" class="btn btn-success">创建任务</button>
            </form>
          </div>
        </div>
      </c:if>
    </div>

    <div class="col-lg-4">
      <div class="card card-glass mb-4">
        <div class="card-header"><h4><i class="bi bi-info-circle"></i> 项目信息</h4></div>
        <div class="card-body">
          <h5><c:out value="${project.projectName}"/></h5>
          <p class="mb-1"><strong>创建人:</strong> <c:out value="${project.creatorName}"/></p>
          <p class="mb-1"><strong>状态:</strong> <span class="badge badge-info">${project.status}</span></p>
          <p class="mb-0"><strong>周期:</strong> <fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd"/></p>
        </div>
      </div>
      <c:if test="${sessionScope.user.hasPermission('project:manage')}">
        <div class="card card-glass mb-4">
          <div class="card-header"><strong><i class="bi bi-toggles"></i> 管理项目状态</strong></div>
          <div class="card-body">
            <form action="projects" method="post" class="form-inline">
              <input type="hidden" name="action" value="update_status">
              <input type="hidden" name="projectId" value="${project.id}">
              <div class="form-group flex-grow-1 mr-2">
                <select name="newStatus" class="form-control">
                  <option value="申请中" ${project.status == '申请中' ? 'selected' : ''}>申请中</option>
                  <option value="进行中" ${project.status == '进行中' ? 'selected' : ''}>进行中</option>
                  <option value="已完成" ${project.status == '已完成' ? 'selected' : ''}>已完成</option>
                  <option value="已归档" ${project.status == '已归档' ? 'selected' : ''}>已归档</option>
                </select>
              </div>
              <button type="submit" class="btn btn-warning">更新</button>
            </form>
          </div>
        </div>
      </c:if>
      <div class="card card-glass mb-4">
        <div class="card-header font-weight-bold"><i class="bi bi-journal-text"></i> 实验方案</div>
        <div class="card-body small">
          <p><strong>目的:</strong> <c:out value="${project.purpose}"/></p>
          <p><strong>步骤:</strong> <pre style="white-space: pre-wrap; font-family: inherit;"><c:out value="${project.procedureSteps}"/></pre></p>
          <p><strong>试剂设备:</strong> <pre style="white-space: pre-wrap; font-family: inherit;"><c:out value="${project.reagentsAndEquipment}"/></pre></p>
        </div>
      </div>
      <c:if test="${sessionScope.user.hasPermission('project:assess_risk')}">
        <div class="card card-glass">
          <div class="card-header"><h4><i class="bi bi-shield-check"></i> AI 风险评估</h4></div>
          <div class="card-body">
            <div id="report-container"></div>
            <div id="report-json" style="display:none;">${project.riskAssessmentReport}</div>
            <hr>
            <form action="assess-risk" method="post">
              <input type="hidden" name="projectId" value="${project.id}">
              <div class="form-group">
                <label for="additionalInfo"><small>补充信息 (可选)</small></label>
                <textarea name="additionalInfo" id="additionalInfo" class="form-control" rows="2" placeholder="输入想让AI特别注意的信息..."></textarea>
              </div>
              <button type="submit" class="btn btn-info">开始AI风险评估</button>
            </form>
          </div>
        </div>
      </c:if>
    </div>
  </div>
</div>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    const reportContainer = document.getElementById('report-container');
    const reportJsonDiv = document.getElementById('report-json');
    if (reportJsonDiv && reportJsonDiv.textContent.trim()) {
      try {
        const reportData = JSON.parse(reportJsonDiv.textContent);
        let html = '';
        if (reportData.error) { html = `<p class="text-danger">${reportData.error}</p>`; }
        else {
          if (reportData.risk_points && reportData.risk_points.length > 0) {
            html += '<h6>识别出的风险点：</h6><ul>';
            reportData.risk_points.forEach(p => { html += `<li><strong>步骤:</strong> ${p.step} <br/> <strong>风险:</strong> ${p.risk} <span class="badge badge-danger">${p.level}</span></li>`; });
            html += '</ul><hr>';
          }
          if (reportData.suggestions && reportData.suggestions.length > 0) {
            html += '<h6>改进建议：</h6><ol>';
            reportData.suggestions.forEach(s => { html += `<li>${s}</li>`; });
            html += '</ol><hr>';
          }
          if (reportData.emergency_plan) {
            html += '<h6>应急预案：</h6>';
            for (const [key, value] of Object.entries(reportData.emergency_plan)) { html += `<p><strong>${key.replace('_',' ')}:</strong> ${value}</p>`; }
          }
        }
        reportContainer.innerHTML = html;
      } catch (e) { reportContainer.innerHTML = '<p class="text-danger">评估报告格式错误，无法解析。</p>'; }
    } else { reportContainer.innerHTML = '<p class="text-muted">暂无评估报告。请提交分析以生成报告。</p>'; }
  });
</script>

<jsp:include page="/WEB-INF/layout/footer.jsp" />