<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 1. 引入公共头部 --%>
<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="仪表盘"/>
</jsp:include>

<%-- 2. 页面的核心内容 --%>
<div class="container" style="padding-top: 2rem; padding-bottom: 2rem;">
  <h2 class="mb-4">欢迎回来, <c:out value="${sessionScope.user.fullName}"/>!</h2>

  <div class="row">
    <div class="col-lg-8">
      <div class="card mb-4 card-glass">
        <div class="card-header font-weight-bold">
          <i class="bi bi-list-task"></i> 我的待办任务
        </div>
        <ul class="list-group list-group-flush">
          <c:if test="${empty myTasks}">
            <li class="list-group-item text-muted" style="background: transparent;">太棒了！您当前没有待办任务。</li>
          </c:if>
          <c:forEach var="task" items="${myTasks}" begin="0" end="4">
            <li class="list-group-item d-flex justify-content-between align-items-center" style="background: transparent;">
              <div>
                <c:out value="${task.taskName}"/>
                <br>
                <small class="text-muted">所属项目: <c:out value="${task.projectName}"/></small>
              </div>
              <span class="badge badge-primary badge-pill">${task.status}</span>
            </li>
          </c:forEach>
          <a href="#" class="list-group-item list-group-item-action text-center text-primary" style="background: transparent;">查看所有任务...</a>
        </ul>
      </div>

      <div class="card mb-4 card-glass">
        <div class="card-header font-weight-bold">
          <i class="bi bi-folder2-open"></i> 我创建的项目
        </div>
        <div class="list-group list-group-flush">
          <c:if test="${empty myProjects}">
            <div class="list-group-item text-muted" style="background: transparent;">您尚未创建任何项目。</div>
          </c:if>
          <c:forEach var="project" items="${myProjects}" begin="0" end="4">
            <a href="projects?action=view&id=${project.id}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" style="background: transparent;">
              <c:out value="${project.projectName}"/>
              <span class="badge badge-info">${project.status}</span>
            </a>
          </c:forEach>
          <a href="projects" class="list-group-item list-group-item-action text-center text-primary" style="background: transparent;">查看所有项目...</a>
        </div>
      </div>
    </div>

    <div class="col-lg-4">
      <div class="card mb-4 card-glass">
        <div class="card-header font-weight-bold"><i class="bi bi-lightning-charge-fill"></i> 快捷操作</div>
        <div class="list-group list-group-flush">
          <a href="projects" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-card-list text-secondary"></i> 查看所有项目</a>
          <c:if test="${sessionScope.user.hasPermission('project:manage')}">
            <a href="projects?action=add_form" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-plus-circle-fill text-success"></i> 创建新项目</a>
          </c:if>
          <a href="equipment" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-calendar2-plus text-info"></i> 设备预约</a>
          <a href="venues" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-calendar2-event text-info"></i> 场地预约</a>
          <a href="supply-request?action=show_form" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-box-seam text-primary"></i> 申领物资</a>
          <c:if test="${sessionScope.user.hasPermission('admin:access')}">
            <a href="admin/userList" class="list-group-item list-group-item-action list-group-item-warning font-weight-bold" style="background: transparent;"><i class="bi bi-gear-fill"></i> 进入后台管理</a>
          </c:if>
        </div>
      </div>

      <div class="card card-glass">
        <div class="card-header font-weight-bold"><i class="bi bi-clock-history"></i> 我未来的预约</div>
        <ul class="list-group list-group-flush small">
          <c:if test="${empty myEquipmentBookings and empty myVenueBookings}">
            <li class="list-group-item text-muted" style="background: transparent;">暂无未来的预约。</li>
          </c:if>
          <c:forEach var="eqBooking" items="${myEquipmentBookings}">
            <li class="list-group-item" style="background: transparent;"><i class="bi bi-cpu"></i> 【设备】<c:out value="${eqBooking.equipmentName}"/>: <fmt:formatDate value="${eqBooking.startTime}" pattern="MM-dd HH:mm"/></li>
          </c:forEach>
          <c:forEach var="vBooking" items="${myVenueBookings}">
            <li class="list-group-item" style="background: transparent;"><i class="bi bi-door-open"></i> 【场地】<c:out value="${vBooking.venueName}"/>: <fmt:formatDate value="${vBooking.startTime}" pattern="MM-dd HH:mm"/></li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</div>

<%-- 3. 引入公共尾部 --%>
<jsp:include page="/WEB-INF/layout/footer.jsp" />