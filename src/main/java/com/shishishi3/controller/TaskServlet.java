package com.shishishi3.controller;

import com.shishishi3.dao.TaskDAO;
import com.shishishi3.model.Task;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * TaskServlet 控制器
 * 使用 @WebServlet("/tasks") 注解将这个 Servlet 映射到 URL /tasks 路径。
 * 它是处理所有与任务相关的用户请求的中心点。
 */
@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskDAO taskDAO;

    // Servlet 初始化时，创建一个 TaskDAO 实例
    public void init() {
        taskDAO = new TaskDAO();
    }

    // 对于 POST 请求，我们简单地让它调用 doGet 方法来处理
    // 这样无论是 GET 还是 POST，处理逻辑都在一个地方
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // 主要的请求处理逻辑
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求字符编码，防止中文乱码
        request.setCharacterEncoding("UTF-8");

        // 获取一个名为 "action" 的参数，来决定要执行哪个操作
        String action = request.getParameter("action");

        // 如果没有 action 参数，我们就默认执行 "list" 操作，即显示任务列表
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    // 显示用于添加新任务的表单页面
                    showNewForm(request, response);
                    break;
                case "insert":
                    // 将新任务数据插入数据库
                    insertTask(request, response);
                    break;
                // 在这里为未来的 "edit", "update", "delete" 等操作预留位置
                default:
                    // 默认操作：列出所有任务
                    listTasks(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * 从数据库获取任务列表，并转发到 tasks.jsp 页面进行显示
     */
    private void listTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 在一个完整的应用中，userId 应该从用户登录后的 Session 中获取。
        // 为了简化，我们这里暂时硬编码为 1。
        // 这体现了“人员权限管理”[cite: 3]中，不同用户看到不同数据的思想。
        int userId = 1;
        List<Task> taskList = taskDAO.getAllTasksByUserId(userId);

        // 将获取到的任务列表存入 request 对象中，以便 JSP 页面可以访问
        request.setAttribute("taskList", taskList);

        // 使用 RequestDispatcher 将请求和响应对象转发到指定的 JSP 页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("tasks.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * 转发到 task-form.jsp 页面，该页面包含一个用于创建新任务的表单
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("task-form.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * 从请求中获取表单数据，创建一个新的 Task 对象，并将其存入数据库
     * [cite_start]这个方法实现了“任务分配与协作”[cite: 3]中的任务创建功能。
     */
    private void insertTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 从 HTTP 请求中获取表单提交的各项数据
        String taskName = request.getParameter("taskName");
        String taskDescription = request.getParameter("taskDescription");
        Date dueDate = Date.valueOf(request.getParameter("dueDate"));
        String priority = request.getParameter("priority");
        int userId = Integer.parseInt(request.getParameter("userId"));
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        // 创建一个新的 Task 对象
        Task newTask = new Task();
        newTask.setTaskName(taskName);
        newTask.setTaskDescription(taskDescription);
        newTask.setDueDate(dueDate);
        newTask.setPriority(priority);
        newTask.setStatus("To Do"); // 新任务的默认状态是“待办”
        newTask.setUserId(userId);
        newTask.setProjectId(projectId);

        // 调用 DAO 将新任务存入数据库
        taskDAO.addTask(newTask);

        // 操作完成后，使用重定向（redirect）到任务列表页面
        // 重定向可以防止用户刷新页面时重复提交表单
        response.sendRedirect("tasks?action=list");
    }
}
