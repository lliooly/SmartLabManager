package com.shishishi3.dao;

import com.shishishi3.model.Task;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDAO 类，封装了对 lab_tasks 表的所有数据库操作 (CRUD)
 */
public class TaskDAO {

    /**
     * 新增一个任务到数据库
     * @param task 要添加的任务对象
     */
    public void addTask(Task task) {
        // SQL 插入语句
        String sql = "INSERT INTO lab_tasks (project_id, user_id, task_name, task_description, due_date, priority, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // 使用 try-with-resources 语句自动关闭资源
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, task.getProjectId());
            pstmt.setInt(2, task.getUserId());
            pstmt.setString(3, task.getTaskName());
            pstmt.setString(4, task.getTaskDescription());
            pstmt.setDate(5, task.getDueDate());
            pstmt.setString(6, task.getPriority());
            pstmt.setString(7, task.getStatus());

            // 执行插入操作
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户ID查询其所有任务
     * @param userId 要查询的用户ID
     * @return 该用户的任务列表
     */
    public List<Task> getAllTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM lab_tasks WHERE user_id = ? ORDER BY due_date ASC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // 遍历结果集，将每一行数据封装成一个 Task 对象
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setProjectId(rs.getInt("project_id"));
                task.setUserId(rs.getInt("user_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setDueDate(rs.getDate("due_date"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // 未来可以继续在这里添加其他方法，例如：
    // public Task getTaskById(int taskId) { ... }
    // public void updateTask(Task task) { ... }
    // public void deleteTask(int taskId) { ... }
}
