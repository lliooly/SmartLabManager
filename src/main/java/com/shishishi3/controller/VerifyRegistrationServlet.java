package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/verify-registration")
public class VerifyRegistrationServlet extends HttpServlet {
    private UserDAO userDAO;
    private static final int DEFAULT_ROLE_ID = 3; // 新用户默认为 'Student'

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String submittedCode = request.getParameter("verificationCode");

        // 安全检查
        if (session == null || session.getAttribute("registration_code") == null) {
            response.sendRedirect("register");
            return;
        }

        String sessionCode = (String) session.getAttribute("registration_code");
        long expiryTime = (long) session.getAttribute("registration_code_expiry");
        User userToRegister = (User) session.getAttribute("registration_user");

        if (System.currentTimeMillis() > expiryTime) {
            request.setAttribute("error", "验证码已过期，请重新注册。");
            request.getRequestDispatcher("register-verify.jsp").forward(request, response);
        } else if (submittedCode != null && submittedCode.equals(sessionCode)) {
            // 验证成功，执行数据库注册
            boolean isSuccess = userDAO.registerUser(userToRegister, DEFAULT_ROLE_ID);

            // 清理Session中的临时数据
            session.removeAttribute("registration_user");
            session.removeAttribute("registration_code");
            session.removeAttribute("registration_code_expiry");

            if (isSuccess) {
                response.sendRedirect("login?reg=success");
            } else {
                // 这种情况很少发生，除非在验证期间用户名被抢注了
                request.setAttribute("error", "注册失败，请重试。");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } else {
            // 验证失败
            request.setAttribute("error", "验证码不正确。");
            request.getRequestDispatcher("register-verify.jsp").forward(request, response);
        }
    }
}