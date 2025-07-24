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
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) return;

        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            int taskId = Integer.parseInt(request.getParameter("taskId"));

            switch (action) {
                case "update_status":
                    String statusName = request.getParameter("status");
                    int statusId = 0;
                    if (statusName != null) {
                        switch (statusName) {
                            case "申请中":
                                statusId = 1;
                                break;
                            case "进行中":
                                statusId = 2;
                                break;
                            case "已完成":
                                statusId = 3;
                                break;
                            case "已归档":
                                statusId = 4;
                                break;
                        }
                    }
                    if (statusId != 0) {
                        taskDAO.updateTaskStatus(taskId, statusId);
                    }
                    break;

                case "delete":
                    taskDAO.deleteTask(taskId);
                    break;
            }
            response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/projects");
        }
    }
}