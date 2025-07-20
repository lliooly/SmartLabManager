package com.shishishi3.controller.admin;

import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/userList")
public class UserListServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 从DAO获取所有用户数据
        List<User> userList = userDAO.getAllUsers();

        // 2. 将用户列表存入request属性
        request.setAttribute("userList", userList);

        // 3. 转发到JSP页面进行渲染
        request.getRequestDispatcher("/admin/user-list.jsp").forward(request, response);
    }
}