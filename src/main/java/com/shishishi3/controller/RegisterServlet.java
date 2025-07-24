package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;
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
    private static final int DEFAULT_ROLE_ID = 3;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // 1. 从表单获取所有信息
        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String submittedCode = request.getParameter("verificationCode");

        // 2. 严格的后端校验
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!isPasswordComplexEnough(password)) {
            request.setAttribute("error", "密码强度不足。密码必须至少6位，且包含数字、大小写字母、特殊符号中的至少两种。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (userDAO.isUserExists(username, email)) {
            request.setAttribute("error", "注册失败，用户名或邮箱已被占用。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 3. 校验验证码
        if (session == null || session.getAttribute("verification_code") == null) {
            request.setAttribute("error", "请先发送并接收验证码。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        String sessionCode = (String) session.getAttribute("verification_code");
        long expiryTime = (long) session.getAttribute("verification_code_expiry");

        if (System.currentTimeMillis() > expiryTime) {
            request.setAttribute("error", "验证码已过期，请重新发送。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!sessionCode.equals(submittedCode)) {
            request.setAttribute("error", "验证码不正确。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 4. 所有验证通过，执行注册
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);

        boolean isSuccess = userDAO.registerUser(newUser, DEFAULT_ROLE_ID);

        if (isSuccess) {
            // 注册成功后，清理Session中的临时验证码信息
            session.removeAttribute("verification_code");
            session.removeAttribute("verification_code_expiry");
            response.sendRedirect("login?reg=success");
        } else {
            request.setAttribute("error", "注册失败，发生未知数据库错误。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private boolean isPasswordComplexEnough(String password) {
        if (password == null || password.length() < 6) return false;
        int categories = 0;
        if (password.matches(".*[A-Z].*")) categories++;
        if (password.matches(".*[a-z].*")) categories++;
        if (password.matches(".*[0-9].*")) categories++;
        if (password.matches(".*[^A-Za-z0-9].*")) categories++;
        return categories >= 2;
    }
}