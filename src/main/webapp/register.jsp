<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>用户注册 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card">
        <div class="card-header">
          <h4>新用户注册</h4>
        </div>
        <div class="card-body">
          <form id="registerForm" action="register" method="post">
            <c:if test="${not empty error}">
              <div class="alert alert-danger"><c:out value="${error}"/></div>
            </c:if>
            <div class="form-group">
              <label for="fullName">姓名</label>
              <input type="text" class="form-control" id="fullName" name="fullName" required>
            </div>
            <div class="form-group">
              <label for="username">用户名</label>
              <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="form-group">
              <label for="email">邮箱</label>
              <input type="email" class="form-control" id="email" name="email" required>
            </div>
            <div class="form-group">
              <label for="password">密码</label>
              <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <div class="form-group">
              <label for="confirmPassword">确认密码</label>
              <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">注册</button>
          </form>
          <div class="text-center mt-3">
            <a href="login">已有账户？直接登录</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  const form = document.getElementById('registerForm');
  form.addEventListener('submit', function(event) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    if (password !== confirmPassword) {
      alert('两次输入的密码不一致！');
      event.preventDefault(); // 阻止表单提交
    }
  });
</script>
</body>
</html>