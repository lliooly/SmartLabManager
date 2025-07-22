package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.util.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/send-code")
public class SendCodeServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1. 检查邮箱是否已被注册
        if (userDAO.isUserExists(null, email)) {
            response.getWriter().write("{\"success\": false, \"message\": \"该邮箱已被注册\"}");
            return;
        }

        // 2. 生成并发送验证码
        String code = EmailService.generateVerificationCode();
        try {
            EmailService.sendVerificationCode(email, code);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"验证码发送失败，请检查邮箱地址\"}");
            return;
        }

        // 3. 将验证码存入Session
        HttpSession session = request.getSession();
        session.setAttribute("verification_code", code);
        session.setAttribute("verification_code_expiry", System.currentTimeMillis() + 10 * 60 * 1000); // 10分钟有效期

        // 4. 返回成功信息
        response.getWriter().write("{\"success\": true, \"message\": \"验证码已发送\"}");
    }
}