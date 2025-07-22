package com.shishishi3.controller;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.dao.ProjectDAO;
import com.shishishi3.model.Project;
import com.shishishi3.model.User;
import com.shishishi3.util.AiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/assess-risk")
public class RiskAssessmentServlet extends HttpServlet {
    private ProjectDAO projectDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
        auditLogDAO = new AuditLogDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String additionalInfo = request.getParameter("additionalInfo");
        User currentUser = (User) request.getSession().getAttribute("user");

        // 1. 从数据库获取完整的项目信息
        Project project = projectDAO.getProjectById(projectId);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "项目未找到");
            return;
        }

        // 2. 拼接所有信息，作为发送给AI的内容
        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("实验项目名称: ").append(project.getProjectName()).append("\n");
        detailsBuilder.append("实验目的: ").append(project.getPurpose()).append("\n");
        detailsBuilder.append("详细操作步骤: ").append(project.getProcedureSteps()).append("\n");
        detailsBuilder.append("所用试剂和设备: ").append(project.getReagentsAndEquipment()).append("\n");
        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            detailsBuilder.append("用户补充信息: ").append(additionalInfo).append("\n");
        }

        // 3. 调用AiService进行分析
        String aiResponseJson = AiService.getRiskAssessment(detailsBuilder.toString());

        // 4. 将AI返回的报告保存回数据库
        projectDAO.saveAssessmentReport(projectId, aiResponseJson);

        // 5. 记录审计日志
        String logMessage = "对项目ID " + projectId + " 执行了AI风险评估。";
        auditLogDAO.logAction(currentUser.getId(), currentUser.getUsername(), logMessage, "Project", projectId,request.getRemoteAddr());

        // 6. 重定向回项目详情页，并附带成功提示
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId + "&assessment=done");
    }
}