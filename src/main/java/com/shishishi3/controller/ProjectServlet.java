package com.shishishi3.controller;

import com.shishishi3.dao.ProjectDAO;
import com.shishishi3.dao.TaskDAO;
import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.Project;
import com.shishishi3.model.Task;
import com.shishishi3.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) { action = "list"; }

        switch (action) {
            case "view": viewProject(request, response); break;
            case "add_form": showNewForm(request, response); break;
            default: listProjects(request, response); break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) { listProjects(request, response); return; }

        switch (action) {
            case "insert": insertProject(request, response); break;
            case "create_task": createTaskForProject(request, response); break;
            case "update_status": updateProjectStatus(request, response); break;
            default: listProjects(request, response); break;
        }
    }


    // --- Private Helper Methods ---

    private void listProjects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Project> projectList = projectDAO.getAllProjects();
        request.setAttribute("projectList", projectList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("project-list.jsp");
        dispatcher.forward(request, response);
    }

    // 在 ProjectServlet.java 中
    private void viewProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int projectId = Integer.parseInt(request.getParameter("id"));
            Project project = projectDAO.getProjectById(projectId);

            // 如果找不到项目，则重定向到列表页并附带错误提示
            if (project == null) {
                response.sendRedirect(request.getContextPath() + "/projects?error=notFound");
                return;
            }

            List<Task> taskList = taskDAO.getTasksByProjectId(projectId);
            List<User> userList = userDAO.getAllUsers();

            request.setAttribute("project", project);
            request.setAttribute("taskList", taskList);
            request.setAttribute("userList", userList);

            String returnUrl = request.getParameter("returnUrl");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                request.setAttribute("returnUrl", returnUrl);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/project-detail.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/projects?error=unknown");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("project-form.jsp");
        dispatcher.forward(request, response);
    }

    private void insertProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Project newProject = new Project();
        newProject.setProjectName(request.getParameter("projectName"));
        newProject.setDescription(request.getParameter("description"));
        newProject.setStartDate(Date.valueOf(request.getParameter("startDate")));
        newProject.setEndDate(Date.valueOf(request.getParameter("endDate")));
        newProject.setPurpose(request.getParameter("purpose"));
        newProject.setProcedureSteps(request.getParameter("procedureSteps"));
        newProject.setReagentsAndEquipment(request.getParameter("reagentsAndEquipment"));

        // 从Session中获取当前登录用户的ID作为创建者ID
        User currentUser = (User) request.getSession().getAttribute("user");
        newProject.setCreatorId(currentUser.getId());

        // 新建项目时，默认状态为“申请中”，ID为1
        newProject.setStatusId(1);

        projectDAO.createProject(newProject);
        response.sendRedirect(request.getContextPath() + "/projects");
    }

    private void createTaskForProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Task newTask = new Task();
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        newTask.setProjectId(projectId);
        newTask.setTaskName(request.getParameter("taskName"));
        newTask.setDescription(request.getParameter("description"));
        newTask.setAssigneeId(Integer.parseInt(request.getParameter("assigneeId")));
        newTask.setPriority(request.getParameter("priority"));

        String dueDateStr = request.getParameter("dueDate");
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            newTask.setDueDate(Date.valueOf(dueDateStr));
        }

        // 新建任务时，默认状态为“待办”，ID为1
        newTask.setStatusId(1);

        taskDAO.createTask(newTask);
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
    }

    private void updateProjectStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            String newStatusName = request.getParameter("newStatus");

            // 检查newStatusName是否为空或乱码（包含非预期字符）
            if (newStatusName == null || newStatusName.trim().isEmpty() ||
                    !newStatusName.matches("^[\\u4e00-\\u9fa5]+$")) { // 仅允许中文
                System.out.println("Invalid newStatusName: " + newStatusName);
                response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId + "&error=invalidStatus");
                return;
            }

            int newStatusId = 0;
            switch (newStatusName) {
                case "申请中": newStatusId = 1; break;
                case "进行中": newStatusId = 2; break;
                case "已完成": newStatusId = 3; break;
                case "已归档": newStatusId = 4; break;
                default:
                    System.out.println("Unsupported newStatusName: " + newStatusName);
                    response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId + "&error=unsupportedStatus");
                    return;
            }

            projectDAO.updateProjectStatus(projectId, newStatusId);
            response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId + "&success=statusUpdated");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/projects?error=updateFailed");
        }
    }
}