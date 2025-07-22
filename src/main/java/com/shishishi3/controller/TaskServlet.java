package com.shishishi3.controller;

import com.shishishi3.dao.TaskDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {
    private TaskDAO taskDAO;

    @Override
    public void init() {
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        // 从隐藏域获取projectId，以便操作完成后能跳回正确的项目详情页
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        if (action != null) {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            switch (action) {
                case "update_status":
                    String newStatus = request.getParameter("status");
                    taskDAO.updateTaskStatus(taskId, newStatus);
                    break;
                case "delete":
                    taskDAO.deleteTask(taskId);
                    break;
            }
        }

        // 所有操作完成后，重定向回项目详情页
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
    }
}