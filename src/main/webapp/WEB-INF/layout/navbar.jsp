<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

<nav class="navbar navbar-expand-lg navbar-light sticky-top navbar-glass-light">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">智能实验室平台</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
      <!-- 左侧主导航链接 -->
      <ul class="navbar-nav mr-auto">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">仪表盘</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/projects">项目管理</a>
        </li>

        <!-- 资源管理下拉菜单 -->
        <c:if test="${sessionScope.user.hasPermission('equipment:view') || sessionScope.user.hasPermission('supply:view') || sessionScope.user.hasPermission('venue:view')}">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarResourceDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              资源管理
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarResourceDropdown">
              <c:if test="${sessionScope.user.hasPermission('equipment:view')}">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/equipment">设备管理</a>
              </c:if>
              <c:if test="${sessionScope.user.hasPermission('supply:view')}">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/supplies">物资管理</a>
              </c:if>
              <c:if test="${sessionScope.user.hasPermission('venue:view')}">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/venues">场地管理</a>
              </c:if>
            </div>
          </li>
        </c:if>

        <!-- 后台管理下拉菜单 -->
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

      <!-- 右侧用户信息与退出 -->
      <ul class="navbar-nav align-items-center">
        <li class="nav-item">
                    <span class="navbar-text">
                        欢迎, <c:out value="${sessionScope.user.fullName}"/>
                    </span>
        </li>
        <li class="nav-item ml-3">
          <a class="btn btn-outline-secondary btn-sm" href="${pageContext.request.contextPath}/logout">退出登录</a>
        </li>
      </ul>
    </div>
  </div>
</nav>