package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;
import com.shishishi3.util.EmailService;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    private static final int DEFAULT_ROLE_ID = 3; // 假设ID为3的角色是 'Student'

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // GET请求直接显示注册页面
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    // POST请求处理注册逻辑
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 检查用户名或邮箱是否已被占用
        if (userDAO.isUserExists(username, email)) { // 您需要在UserDAO中添加这个方法
            request.setAttribute("error", "用户名或邮箱已被占用。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 用户信息合法，开始发送验证码流程
        String code = EmailService.generateVerificationCode();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 将待注册的用户信息和验证码存入Session
        HttpSession session = request.getSession();
        session.setAttribute("registration_user", new User(username, hashedPassword, fullName, email)); // 需要一个带参构造函数
        session.setAttribute("registration_code", code);
        session.setAttribute("registration_code_expiry", System.currentTimeMillis() + 10 * 60 * 1000); // 10分钟有效期

        // 发送邮件
        EmailService.sendVerificationCode(email, code);

        // 重定向到验证码输入页面
        response.sendRedirect("register-verify.jsp");
    }
}