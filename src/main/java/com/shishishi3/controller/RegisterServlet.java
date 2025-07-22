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

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String submittedCode = request.getParameter("verificationCode");

        // 安全检查：确保Session和验证码都存在
        if (session == null || session.getAttribute("verification_code") == null || session.getAttribute("verification_code_expiry") == null) {
            request.setAttribute("error", "验证流程已过期，请重新发送验证码。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String sessionCode = (String) session.getAttribute("verification_code");
        long expiryTime = (long) session.getAttribute("verification_code_expiry");

        // ==================== 新增：校验验证码是否过期 ====================
        if (System.currentTimeMillis() > expiryTime) {
            session.removeAttribute("verification_code"); // 让旧验证码失效
            session.removeAttribute("verification_code_expiry");
            request.setAttribute("error", "验证码已过期，请重新发送。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        // =================================================================

        if (!sessionCode.equals(submittedCode)) {
            request.setAttribute("error", "验证码不正确。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 验证码正确且未过期，执行注册
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
            request.setAttribute("error", "注册失败，用户名或邮箱可能已被占用。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}