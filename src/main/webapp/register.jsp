<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <title>用户注册 - 智能实验室管理平台</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/custom.css">
  <style>
    .register-container { display: flex; align-items: center; justify-content: center; min-height: 100vh; padding: 2rem 0; }
    .register-form-container { width: 100%; max-width: 500px; }
    #verification-group { display: none; } /* 默认隐藏验证码输入框 */
  </style>
</head>
<body id="page-top">
<div class="register-container">
  <div class="register-form-container">
    <div class="card card-glass p-4">
      <div class="card-body">
        <h3 class="card-title text-center mb-4"><i class="bi bi-person-plus-fill"></i> 新用户注册</h3>
        <form id="registerForm" action="register" method="post">
          <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
          <div id="email-error" class="alert alert-danger" style="display: none;"></div>

          <div class="form-group"><label>姓名</label><input type="text" class="form-control" name="fullName" required></div>
          <div class="form-group"><label>用户名</label><input type="text" class="form-control" name="username" required></div>
          <div class="form-group"><label>邮箱</label><input type="email" class="form-control" name="email" id="email" required></div>
          <div class="form-group"><label>密码</label><input type="password" class="form-control" name="password" id="password" required></div>
          <div class="form-group"><label>确认密码</label><input type="password" class="form-control" name="confirmPassword" id="confirmPassword" required></div>

          <div class="input-group mb-3">
            <input type="text" class="form-control" name="verificationCode" placeholder="6位邮箱验证码" required>
            <div class="input-group-append">
              <button class="btn btn-outline-secondary" type="button" id="sendCodeBtn">发送验证码</button>
            </div>
          </div>

          <button type="submit" class="btn btn-primary btn-block">立即注册</button>
          <div class="text-center mt-3"><a href="login">已有账户？直接登录</a></div>
        </form>
      </div>
    </div>
  </div>
</div>

<script>
  document.getElementById('sendCodeBtn').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const btn = this;
    const emailErrorDiv = document.getElementById('email-error');

    if (!email) {
      emailErrorDiv.textContent = '请输入邮箱地址。';
      emailErrorDiv.style.display = 'block';
      return;
    }

    btn.disabled = true;
    btn.textContent = '发送中...';
    emailErrorDiv.style.display = 'none';

    // 使用Fetch API发起AJAX请求
    fetch('send-code', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'email=' + encodeURIComponent(email)
    })
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                alert('验证码已成功发送到您的邮箱！');
                let countdown = 60;
                btn.textContent = countdown + 's后重发';
                const interval = setInterval(function() {
                  countdown--;
                  if (countdown <= 0) {
                    clearInterval(interval);
                    btn.textContent = '发送验证码';
                    btn.disabled = false;
                  } else {
                    btn.textContent = countdown + 's后重发';
                  }
                }, 1000);
              } else {
                emailErrorDiv.textContent = data.message;
                emailErrorDiv.style.display = 'block';
                btn.textContent = '发送验证码';
                btn.disabled = false;
              }
            })
            .catch(error => {
              emailErrorDiv.textContent = '请求失败，请检查网络。';
              emailErrorDiv.style.display = 'block';
              btn.textContent = '发送验证码';
              btn.disabled = false;
            });
  });
</script>
</body>
</html>