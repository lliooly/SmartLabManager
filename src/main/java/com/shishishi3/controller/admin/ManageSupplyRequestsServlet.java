package com.shishishi3.controller.admin;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.dao.SupplyRequestDAO;
import com.shishishi3.model.SupplyRequest;
import com.shishishi3.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/manage-requests")
public class ManageSupplyRequestsServlet extends HttpServlet {
    private SupplyRequestDAO requestDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() {
        requestDAO = new SupplyRequestDAO();
        auditLogDAO = new AuditLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SupplyRequest> pendingRequests = requestDAO.getAllPendingRequests();
        request.setAttribute("pendingRequests", pendingRequests);
        request.getRequestDispatcher("/admin/manage-requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String newStatus = request.getParameter("status"); // "已批准" 或 "已驳回"
        User adminUser = (User) request.getSession().getAttribute("user");

        requestDAO.updateRequestStatus(requestId, newStatus, adminUser.getId());

        // 记录审计日志
        String logMessage = "审批了物资申领ID " + requestId + ", 状态变更为: " + newStatus;
        auditLogDAO.logAction(adminUser.getId(), adminUser.getUsername(), logMessage, "SupplyRequest", requestId,request.getRemoteAddr());

        response.sendRedirect(request.getContextPath() + "/admin/manage-requests");
    }
}