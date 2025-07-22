package com.shishishi3.controller.admin;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.dao.PermissionDAO;
import com.shishishi3.dao.RoleDAO;
import com.shishishi3.model.Permission;
import com.shishishi3.model.Role;
import com.shishishi3.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/manageRolePermissions")
public class RolePermissionManagementServlet extends HttpServlet {
    private RoleDAO roleDAO;
    private PermissionDAO permissionDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() {
        roleDAO = new RoleDAO();
        permissionDAO = new PermissionDAO();
        auditLogDAO = new AuditLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String returnUrl = request.getParameter("returnUrl"); // 新增

        Role targetRole = roleDAO.getRoleById(roleId);
        List<Permission> allPermissions = permissionDAO.getAllPermissions();
        Set<Integer> currentPermissionIds = permissionDAO.getPermissionIdsForRole(roleId);

        request.setAttribute("targetRole", targetRole);
        request.setAttribute("allPermissions", allPermissions);
        request.setAttribute("currentPermissionIds", currentPermissionIds);
        request.setAttribute("returnUrl", returnUrl); // 新增

        request.getRequestDispatcher("/admin/manage-role-permissions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String[] permissionIds = request.getParameterValues("permissionIds");

        roleDAO.updateRolePermissions(roleId, permissionIds);

        User adminUser = (User) request.getSession().getAttribute("user");
        String logMessage = "更新了角色ID " + roleId + " 的权限。新的权限ID: " + (permissionIds != null ? Arrays.toString(permissionIds) : "无");
        auditLogDAO.logAction(adminUser.getId(), adminUser.getUsername(), logMessage, "RolePermissions", roleId, request.getRemoteAddr());

        response.sendRedirect(request.getContextPath() + "/admin/roleList");
    }
}