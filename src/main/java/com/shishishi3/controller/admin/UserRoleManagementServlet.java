package com.shishishi3.controller.admin;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.dao.RoleDAO;
import com.shishishi3.dao.UserDAO;
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
import java.util.stream.Collectors;

@WebServlet("/admin/manageUserRoles")
public class UserRoleManagementServlet extends HttpServlet {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
        auditLogDAO = new AuditLogDAO();
    }

    /**
     * 处理显示用户角色编辑页面的GET请求
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String returnUrl = request.getParameter("returnUrl"); // 新增：获取returnUrl参数

            User targetUser = userDAO.getUserById(userId);
            List<Role> allRoles = roleDAO.getAllRoles();
            Set<Role> currentUserRoles = roleDAO.getRolesByUserId(userId);
            Set<Integer> currentUserRoleIds = currentUserRoles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            request.setAttribute("targetUser", targetUser);
            request.setAttribute("allRoles", allRoles);
            request.setAttribute("currentUserRoleIds", currentUserRoleIds);
            request.setAttribute("returnUrl", returnUrl); // 新增：将returnUrl传递给JSP

            request.getRequestDispatcher("/admin/manage-user-roles.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的用户ID。");
        }
    }

    /**
     * 处理保存用户角色更改的POST请求
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String[] selectedRoleIds = request.getParameterValues("roleIds");

        // 调用DAO更新数据库（此方法使用事务，保证数据一致性）
        userDAO.updateUserRoles(userId, selectedRoleIds);

        // 记录审计日志
        User adminUser = (User) request.getSession().getAttribute("user");
        String logMessage = "更新了用户ID " + userId + " 的角色。新的角色ID: " + (selectedRoleIds != null ? Arrays.toString(selectedRoleIds) : "无");
        auditLogDAO.logAction(adminUser.getId(), adminUser.getUsername(), logMessage, "UserRoles", userId,request.getRemoteAddr());

        // 操作完成后，重定向回用户列表页
        response.sendRedirect(request.getContextPath() + "/admin/userList");
    }
}