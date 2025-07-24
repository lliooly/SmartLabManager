package com.shishishi3.controller;

import com.shishishi3.service.ProjectService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/assess-risk")
public class AssessRiskServlet extends HttpServlet {
    private ProjectService projectService;

    @Override
    public void init() {
        projectService = new ProjectService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            String additionalNotes = request.getParameter("additionalNotes");

            // 调用Service层处理所有复杂的业务
            projectService.assessProjectRisk(projectId, additionalNotes);

            // **核心改动：不再返回JSON，而是执行页面重定向**
            // 这会让浏览器刷新项目详情页，并附带一个参数提示操作完成
            response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId + "&assessment=done");

        } catch (Exception e) {
            e.printStackTrace();
            // 如果发生错误，也重定向回项目列表页，并附带错误提示
            response.sendRedirect(request.getContextPath() + "/projects?error=assessmentFailed");
        }
    }
}