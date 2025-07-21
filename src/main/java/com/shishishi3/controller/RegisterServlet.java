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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

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

        if (userDAO.isUserExists(username, email)) {
            request.setAttribute("error", "注册失败，用户名或邮箱已被占用。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String code = EmailService.generateVerificationCode();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        HttpSession session = request.getSession();
        session.setAttribute("registration_user", new User(username, hashedPassword, fullName, email));
        session.setAttribute("registration_code", code);
        session.setAttribute("registration_code_expiry", System.currentTimeMillis() + 10 * 60 * 1000); // 10分钟有效期

        try {
            EmailService.sendVerificationCode(email, code);
        } catch (Exception e) {
            e.printStackTrace(); // 在服务器控制台打印详细错误，便于排查
            request.setAttribute("error", "验证码邮件发送失败，请检查邮箱地址或联系管理员。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("register-verify.jsp");
    }
}