package com.shishishi3.controller.admin;

import com.shishishi3.dao.RoleDAO;
import com.shishishi3.model.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/roleList")
public class RoleListServlet extends HttpServlet {
    private RoleDAO roleDAO;

    @Override
    public void init() {
        roleDAO = new RoleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Role> allRoles = roleDAO.getAllRoles();
        request.setAttribute("allRoles", allRoles);
        request.getRequestDispatcher("/admin/role-list.jsp").forward(request, response);
    }
}