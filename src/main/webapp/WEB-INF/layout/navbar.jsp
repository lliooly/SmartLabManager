<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 更新后的CSS样式 --%>
<style>
  .navbar-glass-light {
    /* 1. 将背景色改为半透明的白色 */
    background-color: rgba(255, 255, 255, 0.75);

    /* 核心：应用背景模糊效果 */
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);

    /* 边框也改为深色透明，以搭配浅色背景 */
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }
</style>

<%-- 1. navbar-dark 改为 navbar-light
     2. bg-dark 改为我们自定义的 navbar-glass-light --%>
<nav class="navbar navbar-expand-lg navbar-light sticky-top navbar-glass-light">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">智能实验室平台</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">仪表盘</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/projects">项目管理</a>
        </li>

        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarResourceDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            资源管理
          </a>
          <div class="dropdown-menu" aria-labelledby="navbarResourceDropdown">
            <a class="dropdown-item" href="${pageContext.request.contextPath}/equipment">设备管理</a>
            <a class="dropdown-item" href="${pageContext.request.contextPath}/supplies">物资管理</a>
            <a class="dropdown-item" href="${pageContext.request.contextPath}/venues">场地管理</a>
          </div>
        </li>

        <c:if test="${sessionScope.user.hasPermission('admin:access')}">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarAdminDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              后台管理
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarAdminDropdown">
              <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/userList">用户管理</a>
              <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/roleList">角色与权限</a>
              <c:if test="${sessionScope.user.hasPermission('log:view')}">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/audit-log">审计日志</a>
              </c:if>
            </div>
          </li>
        </c:if>
      </ul>

      <%-- 3. 在ul标签上增加 align-items-center 类来实现垂直居中对齐 --%>
      <ul class="navbar-nav align-items-center">
        <li class="nav-item">
                    <span class="navbar-text">
                        欢迎, <c:out value="${sessionScope.user.fullName}"/>
                    </span>
        </li>
        <li class="nav-item ml-3">
          <%-- 将按钮颜色从 outline-light 改为 outline-secondary 以搭配浅色背景 --%>
          <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>