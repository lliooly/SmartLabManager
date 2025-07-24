<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 1. 引入公共头部 --%>
<jsp:include page="/WEB-INF/layout/header.jsp">
  <jsp:param name="pageTitle" value="仪表盘"/>
</jsp:include>

<%-- 2. 页面的核心内容 --%>
<div class="container main-content">
  <h2 class="mb-4">欢迎回来, <c:out value="${sessionScope.user.fullName}"/>!</h2>

  <div class="row">
    <div class="col-lg-8">
      <div class="row">
        <div class="col-md-6 mb-4">
          <div class="card card-glass h-100">
            <div class="card-body">
              <h5 class="card-title"><i class="bi bi-pie-chart-fill"></i> 项目状态分布</h5>
              <div id="projectStatusChart" style="width: 100%; height: 300px;"></div>
            </div>
          </div>
        </div>
        <div class="col-md-6 mb-4">
          <div class="card card-glass h-100">
            <div class="card-body">
              <h5 class="card-title"><i class="bi bi-bar-chart-line-fill"></i> 设备预约趋势</h5>
              <div id="monthlyBookingsChart" style="width: 100%; height: 300px;"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="card mb-4 card-glass">
        <div class="card-header font-weight-bold"><i class="bi bi-list-task"></i> 我的待办任务</div>
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
              <span class="badge badge-primary badge-pill">${task.statusName}</span> <%-- 修正后的代码 --%>
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
            <a href="projects?action=view&id=${project.id}&returnUrl=${pageContext.request.contextPath}/dashboard"class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" style="background: transparent;">
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
            <a href="projects?action=add_form" class="list-group-item list-group-item-action" style="background: transparent;"><i class="bi bi-plus-circle" style="color: darkgreen"></i>创建新项目</a>
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

<script>
  document.addEventListener("DOMContentLoaded", function() {
    var projectStatusChartDom = document.getElementById('projectStatusChart');
    var monthlyBookingsChartDom = document.getElementById('monthlyBookingsChart');

    if (!projectStatusChartDom || !monthlyBookingsChartDom) {
      console.error("Chart containers not found!");
      return;
    }

    var projectStatusChart = echarts.init(projectStatusChartDom);
    var monthlyBookingsChart = echarts.init(monthlyBookingsChartDom);

    fetch('${pageContext.request.contextPath}/api/dashboard-data')
            .then(response => {
              if (!response.ok) {
                throw new Error('Network response was not ok');
              }
              return response.json();
            })
            .then(data => {
              // 项目状态饼图
              const projectData = Object.entries(data.projectStatus).map(([name, value]) => ({ name, value }));
              const projectOption = {
                tooltip: { trigger: 'item', formatter: '{b} : {c} ({d}%)' },
                legend: { top: 'bottom', textStyle: { color: '#333' } },
                series: [{
                  name: '项目状态', type: 'pie', radius: ['40%', '70%'],
                  avoidLabelOverlap: false, itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
                  label: { show: false }, emphasis: { label: { show: true, fontSize: 18, fontWeight: 'bold' } },
                  data: projectData
                }]
              };
              projectStatusChart.setOption(projectOption);

              // 设备预约趋势折线图
              const bookingMonths = Object.keys(data.monthlyBookings);
              const bookingCounts = Object.values(data.monthlyBookings);
              const bookingOption = {
                tooltip: { trigger: 'axis' },
                xAxis: { type: 'category', data: bookingMonths, axisLine: { lineStyle: { color: '#333' } } },
                yAxis: { type: 'value', axisLine: { lineStyle: { color: '#333' } } },
                series: [{
                  name: '预约次数', data: bookingCounts, type: 'line', smooth: true,
                  itemStyle: { color: '#007bff' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(0, 123, 255, 0.5)' }, { offset: 1, color: 'rgba(0, 123, 255, 0.1)' }]) }
                }],
                grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true }
              };
              monthlyBookingsChart.setOption(bookingOption);
            })
            .catch(error => console.error('Error fetching dashboard data:', error));

    window.addEventListener('resize', function() {
      projectStatusChart.resize();
      monthlyBookingsChart.resize();
    });
  });
</script>

<%-- 3. 引入公共尾部 --%>
<jsp:include page="/WEB-INF/layout/footer.jsp" />