<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="项目详情"/>
</jsp:include>

<div class="container main-content">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3><i class="bi bi-folder2-open"></i> 项目详情</h3>
    <c:url var="fallbackUrl" value="/projects"/>
    <a href="${not empty param.returnUrl ? param.returnUrl : fallbackUrl}" class="btn btn-outline-secondary mb-3">
      <i class="bi bi-arrow-left"></i> 返回
    </a>
  </div>
  <c:if test="${param.assessment == 'done'}"><div class="alert alert-success">AI风险评估已完成并保存！</div></c:if>

  <div class="row">
    <div class="col-lg-8">
      <div class="card card-glass mb-4">
        <div class="card-header font-weight-bold"><i class="bi bi-list-task"></i> 项目任务</div>
        <div class="card-body">
          <table class="table table-sm table-hover">
            <thead class="thead-light">
            <tr><th>任务名称</th><th>负责人</th><th>截止日期</th><th>状态</th><th>操作</th></tr>
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
                        <option value="申请中" ${task.statusName == '申请中' ? 'selected' : ''}>申请中</option>
                        <option value="进行中" ${task.statusName == '进行中' ? 'selected' : ''}>进行中</option>
                        <option value="已完成" ${task.statusName == '已完成' ? 'selected' : ''}>已完成</option>
                        <option value="已归档" ${task.statusName == '已归档' ? 'selected' : ''}>已归档</option>
                      </select>
                    </form>
                  </c:if>
                  <c:if test="${!sessionScope.user.hasPermission('task:edit')}"><c:out value="${task.statusName}"/></c:if>
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

      <div class="card card-glass mb-4">
        <div class="card-header font-weight-bold"><i class="bi bi-body-text"></i> 实验方案详情</div>
        <div class="card-body">
          <h5>实验目的</h5>
          <p><c:out value="${requestScope.project.purpose}" default="未填写"/></p>
          <hr>
          <h5>详细操作步骤</h5>
          <p style="white-space: pre-wrap;"><c:out value="${requestScope.project.procedureSteps}" default="未填写"/></p>
          <hr>
          <h5>所用试剂和设备</h5>
          <p style="white-space: pre-wrap;"><c:out value="${requestScope.project.reagentsAndEquipment}" default="未填写"/></p>
        </div>
      </div>

      <c:if test="${sessionScope.user.hasPermission('task:create')}">
        <div class="card card-glass">
          <div class="card-header font-weight-bold"><i class="bi bi-plus-circle"></i> 添加新任务</div>
          <div class="card-body">
            <form action="projects?action=create_task" method="post">
              <input type="hidden" name="projectId" value="${project.id}">
              <div class="form-group"><label>任务名称</label><input type="text" name="taskName" class="form-control" required></div>
              <div class="form-row">
                <div class="form-group col-md-4">
                  <label>分配给</label>
                  <select name="assigneeId" class="form-control">
                    <option value="0">-- 未分配 --</option>
                    <c:forEach var="user" items="${userList}"><option value="${user.id}">${user.fullName}</option></c:forEach>
                  </select>
                </div>
                <div class="form-group col-md-4"><label>截止日期</label><input type="date" name="dueDate" class="form-control"></div>
                <div class="form-group col-md-4">
                  <label>优先级</label>
                  <select name="priority" class="form-control"><option>低</option><option selected>中</option><option>高</option></select>
                </div>
              </div>
              <button type="submit" class="btn btn-success">创建任务</button>
            </form>
          </div>
        </div>
      </c:if>
    </div>

    <%-- 右侧信息栏 --%>
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
            <form action="projects" method="post">
              <input type="hidden" name="action" value="update_status">
              <input type="hidden" name="projectId" value="${project.id}">
              <div class="form-group"><select name="newStatus" class="form-control">
                <option value="申请中" ${project.status == '申请中' ? 'selected' : ''}>申请中</option>
                <option value="进行中" ${project.status == '进行中' ? 'selected' : ''}>进行中</option>
                <option value="已完成" ${project.status == '已完成' ? 'selected' : ''}>已完成</option>
                <option value="已归档" ${project.status == '已归档' ? 'selected' : ''}>已归档</option>
              </select></div>
              <button type="submit" class="btn btn-warning btn-block">更新</button>
            </form>
          </div>
        </div>
      </c:if>

      <%-- ==================== AI 风险评估 (传统同步版) ==================== --%>
      <c:if test="${sessionScope.user.hasPermission('project:assess_risk')}">
        <div class="card card-glass">
          <div class="card-header"><h4><i class="bi bi-shield-check"></i> AI 风险评估</h4></div>
          <div class="card-body">
              <%-- 报告显示区域 --%>
            <div id="report-container"><p class="text-muted">暂无评估报告。请提交分析以生成报告。</p></div>
            <hr>

              <%-- **修正1: 恢复为传统的form提交方式** --%>
            <form action="assess-risk" method="post">
              <input type="hidden" name="projectId" value="${requestScope.project.id}">
              <div class="form-group">
                <label for="additionalNotes"><strong>补充说明（可选）</strong></label>
                <textarea name="additionalNotes" id="additionalNotes" class="form-control" rows="3" placeholder="如果项目信息不够详细，可在此处添加额外备注以提高AI评估准确度..."></textarea>
              </div>
              <button type="submit" class="btn btn-info">更新并重新评估</button>
            </form>
          </div>
        </div>
      </c:if>
    </div>
  </div>
</div>

<%
  // 在JSP中使用Java代码直接处理，避开EL表达式限制
  String riskReport = "";
  if(request.getAttribute("project") != null) {
    Object project = request.getAttribute("project");
    // 注意：请将下面的类路径修改为你实际的Project类路径
    riskReport = ((com.shishishi3.model.Project)project).getRiskAssessmentReport();

    // 处理换行符和转义
    if(riskReport != null) {
      // 移除所有换行符和回车符
      riskReport = riskReport.replace("\n", "").replace("\r", "");
      // 转义双引号等特殊字符
      riskReport = riskReport.replace("\\", "\\\\")
              .replace("\"", "\\\"")
              .replace("\b", "\\b")
              .replace("\f", "\\f")
              .replace("\n", "\\n")
              .replace("\r", "\\r")
              .replace("\t", "\\t");
    } else {
      riskReport = "";
    }
  }
%>

<script>
  document.addEventListener("DOMContentLoaded", function() {
    const reportContainer = document.getElementById('report-container');

    // 直接使用Java预处理后的变量
    const jsonStringFromServer = '<%= riskReport %>';

    // 处理HTML实体编码
    const processedJson = jsonStringFromServer.replace(/&#034;/g, '"');

    if (processedJson && processedJson.trim() && processedJson.trim() !== 'null') {
      try {
        const reportData = JSON.parse(processedJson);
        reportContainer.innerHTML = '';

        if (reportData.error) {
          const errorP = document.createElement('p');
          errorP.className = 'text-danger';
          errorP.textContent = '评估错误: ' + reportData.error;
          reportContainer.appendChild(errorP);
        } else {
          if (reportData.risk_assessment && reportData.risk_assessment.length > 0) {
            const h6 = document.createElement('h6');
            h6.textContent = '风险评估与防护建议';
            const dl = document.createElement('dl');

            reportData.risk_assessment.forEach(p => {
              const dt = document.createElement('dt');
              const dd = document.createElement('dd');

              dt.innerHTML = p.risk_item + ' <span class="badge badge-danger">' + p.risk_level + '</span>';
              dd.textContent = '防护建议: ' + p.protective_measure;

              dl.appendChild(dt);
              dl.appendChild(dd);
            });

            reportContainer.appendChild(h6);
            reportContainer.appendChild(dl);
            reportContainer.appendChild(document.createElement('hr'));
          }

          if (reportData.emergency_plan) {
            const h6_plan = document.createElement('h6');
            h6_plan.textContent = '应急预案与事故处理';

            const p_guide = document.createElement('p');
            p_guide.textContent = '应对流程指引: ' + reportData.emergency_plan.procedure_guidance;

            const p_analysis = document.createElement('p');
            p_analysis.textContent = '事故记录与分析: ' + reportData.emergency_plan.accident_record_analysis;

            reportContainer.appendChild(h6_plan);
            reportContainer.appendChild(p_guide);
            reportContainer.appendChild(p_analysis);
          }
        }
      } catch (e) {
        reportContainer.innerHTML = '<p class="text-danger">评估报告格式损坏，无法解析。错误: ' + e.message + '</p>';
        console.error('JSON解析错误:', e, '处理后的字符串:', processedJson);
      }
    }
  });
</script>

<jsp:include page="/WEB-INF/layout/footer.jsp" />