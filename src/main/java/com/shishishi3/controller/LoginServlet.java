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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        // 使用重构后的 UserDAO
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET 请求用于显示登录页面
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 调用重构后的核心登录方法
        User user = userDAO.loginAndGetPermissions(username, password);

        if (user != null) {
            // 登录成功
            HttpSession session = request.getSession();
            // 将包含所有权限的完整User对象存入Session
            session.setAttribute("user", user);
            // 重定向到主功能页面
            response.sendRedirect("dashboard" );
        } else {
            // 登录失败
            request.setAttribute("error", "用户名或密码错误，请重试。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}