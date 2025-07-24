package com.shishishi3.controller;

import com.shishishi3.dao.ProjectDAO;
import com.shishishi3.model.Project;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/get-report")
public class GetAiReportServlet extends HttpServlet {
    private ProjectDAO projectDAO;

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // ==================== 核心修正点 开始 ====================
        String projectIdStr = request.getParameter("projectId");

        // 1. 检查 projectId 参数是否存在且不为空字符串
        if (projectIdStr == null || projectIdStr.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 返回 400 错误码
            out.print("{\"error\":\"错误：请求中缺少有效的项目ID (projectId)。\"}");
            out.flush();
            return; // 提前退出，不再执行后续代码
        }
        // ==================== 核心修正点 结束 ====================

        try {
            int projectId = Integer.parseInt(projectIdStr);
            Project project = projectDAO.getProjectById(projectId);

            String reportJson = (project != null) ? project.getRiskAssessmentReport() : null;
            if (reportJson == null || reportJson.isBlank()) {
                reportJson = "{\"error\":\"数据库中没有评估报告或报告为空。\"}";
            }

            out.print(reportJson);
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"错误：项目ID (projectId) 必须是一个有效的数字。\"}");
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"获取报告时服务器发生未知错误。\"}");
            e.printStackTrace();
        }
    }
}