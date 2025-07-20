package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;
    // 新注册用户的默认角色ID，我们设定 3 = Student
    private static final int DEFAULT_ROLE_ID = 3;

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

        // 调用新的DAO方法检查用户是否存在
        if (userDAO.isUserExists(username, email)) {
            request.setAttribute("error", "注册失败，用户名或邮箱已被占用。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 使用 jBCrypt 对密码进行哈希处理
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);

        // 调用重构后的DAO方法进行注册
        boolean isSuccess = userDAO.registerUser(newUser, DEFAULT_ROLE_ID);

        if (isSuccess) {
            // 注册成功，重定向到登录页并附带成功提示
            response.sendRedirect("login?reg=success");
        } else {
            // 注册失败（通常是数据库层面的意外错误）
            request.setAttribute("error", "注册失败，发生未知错误，请稍后重试。");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}