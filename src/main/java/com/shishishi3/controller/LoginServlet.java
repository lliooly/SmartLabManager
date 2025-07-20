package com.shishishi3.controller;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;
import com.shishishi3.util.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // 显示登录页面的GET请求
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    // 处理登录表单提交的POST请求
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 直接调用 UserDAO 中获取完整用户（含权限）的方法
        User user = userDAO.loginAndGetPermissions(username, password);

        if (user != null) {
            // 登录成功
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("tasks"); // 重定向到主功能页面
        } else {
            // 登录失败
            request.setAttribute("error", "用户名或密码错误，请重试。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
