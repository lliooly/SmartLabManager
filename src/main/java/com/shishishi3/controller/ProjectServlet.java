package com.shishishi3.controller;

import com.shishishi3.dao.AuditLogDAO;
import com.shishishi3.dao.ProjectDAO;
import com.shishishi3.dao.TaskDAO;
import com.shishishi3.dao.UserDAO;
import com.shishishi3.model.Project;
import com.shishishi3.model.Task;
import com.shishishi3.model.User;

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
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // 健壮的写法：如果action是null，则默认为"list"
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add_form":
                showAddForm(request, response);
                break;
            case "view":
                viewProject(request, response);
                break;
            case "list":
            default: // 任何其他情况都默认显示列表
                listProjects(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertProject(request, response);
        } else if ("create_task".equals(action)) {
            createTaskForProject(request, response);
        } else if ("update_status".equals(action)) { // 新增：处理更新状态的请求
            updateProjectStatus(request, response);
        }
    }

    // 新增一个私有方法来处理状态更新逻辑
    private void updateProjectStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String newStatus = request.getParameter("newStatus");
        User currentUser = (User) request.getSession().getAttribute("user");

        // 更新数据库
        projectDAO.updateProjectStatus(projectId, newStatus);

        // 记录审计日志
        // 您需要在Servlet的init()方法中初始化 auditLogDAO = new AuditLogDAO();
        String logMessage = "更新了项目ID " + projectId + " 的状态为: " + newStatus;
        auditLogDAO.logAction(currentUser.getId(), currentUser.getUsername(), logMessage, "Project", projectId,request.getRemoteAddr());

        // 操作完成后，重定向回项目详情页
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
    }

    private void listProjects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Project> projectList = projectDAO.getAllProjects();
        request.setAttribute("projectList", projectList);
        request.getRequestDispatcher("/project-list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/project-form.jsp").forward(request, response);
    }

    private void insertProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Project project = new Project();
        project.setProjectName(request.getParameter("projectName"));
        project.setDescription(request.getParameter("description"));

        User currentUser = (User) request.getSession().getAttribute("user");
        project.setCreatorId(currentUser.getId());

        project.setStatus("申请中");

        String startDateStr = request.getParameter("startDate");
        if (startDateStr != null && !startDateStr.isEmpty()) {
            project.setStartDate(Date.valueOf(startDateStr));
        }

        String endDateStr = request.getParameter("endDate");
        if (endDateStr != null && !endDateStr.isEmpty()) {
            project.setEndDate(Date.valueOf(endDateStr));
        }

        // 从request中获取新增的表单字段值，并设置到project对象中
        project.setPurpose(request.getParameter("purpose"));
        project.setProcedureSteps(request.getParameter("procedureSteps"));
        project.setReagentsAndEquipment(request.getParameter("reagentsAndEquipment"));

        request.getSession().setAttribute("successMessage", "新项目【" + project.getProjectName() + "】已成功创建！");
        response.sendRedirect(request.getContextPath() + "/projects");
    }

    private void viewProject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int projectId = Integer.parseInt(request.getParameter("id"));

        Project project = projectDAO.getProjectById(projectId);
        List<Task> taskList = taskDAO.getTasksByProjectId(projectId);
        List<User> userList = userDAO.getAllUsers();

        String returnUrl = request.getParameter("returnUrl");

        request.setAttribute("project", project);
        request.setAttribute("taskList", taskList);
        request.setAttribute("userList", userList);
        request.setAttribute("returnUrl", returnUrl);

        request.getRequestDispatcher("/project-detail.jsp").forward(request, response);
    }

    private void createTaskForProject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        Task task = new Task();
        task.setProjectId(projectId);
        task.setTaskName(request.getParameter("taskName"));
        task.setDescription(request.getParameter("description"));
        task.setAssigneeId(Integer.parseInt(request.getParameter("assigneeId")));
        task.setPriority(request.getParameter("priority"));
        task.setStatus("待办");
        String dueDateStr = request.getParameter("dueDate");
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            task.setDueDate(Date.valueOf(dueDateStr));
        }
        taskDAO.createTask(task);
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
    }
}