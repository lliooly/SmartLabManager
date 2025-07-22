package com.shishishi3.controller.admin;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.model.AuditLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/audit-log")
public class AuditLogServlet extends HttpServlet {
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() { auditLogDAO = new AuditLogDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        Date startDate = null;
        Date endDate = null;

        // 安全地处理日期转换
        try {
            if (request.getParameter("startDate") != null && !request.getParameter("startDate").isEmpty()) {
                startDate = Date.valueOf(request.getParameter("startDate"));
            }
            if (request.getParameter("endDate") != null && !request.getParameter("endDate").isEmpty()) {
                endDate = Date.valueOf(request.getParameter("endDate"));
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "日期格式无效，请输入 yyyy-MM-dd 格式。");
        }

        List<AuditLog> logs = auditLogDAO.searchLogs(username, startDate, endDate);
        request.setAttribute("logs", logs);
        request.getRequestDispatcher("/admin/audit-log.jsp").forward(request, response);
    }
}