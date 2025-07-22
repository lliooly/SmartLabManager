package com.shishishi3.dao;

import com.shishishi3.model.Task;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /**
     * 根据项目ID，获取该项目下的所有任务
     * @param projectId 项目ID
     * @return 任务列表
     */
    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT t.*, u.full_name as assignee_name FROM tasks t " +
                "LEFT JOIN users u ON t.assignee_id = u.id " +
                "WHERE t.project_id = ? ORDER BY t.due_date ASC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));
                task.setProjectId(rs.getInt("project_id"));
                task.setAssigneeId(rs.getInt("assignee_id"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                task.setAssigneeName(rs.getString("assignee_name"));
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * 创建一个新任务
     * @param task 包含新任务信息的对象
     */
    public void createTask(Task task) {
        String sql = "INSERT INTO tasks (task_name, description, project_id, assignee_id, priority, status, due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getProjectId());
            // 处理负责人ID可能为空的情况
            if (task.getAssigneeId() == 0) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, task.getAssigneeId());
            }
            pstmt.setString(5, task.getPriority());
            pstmt.setString(6, task.getStatus());
            pstmt.setDate(7, task.getDueDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新一个任务的状态
     * @param taskId    要更新的任务ID
     * @param newStatus 新的状态
     */
    public void updateTaskStatus(int taskId, String newStatus) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID删除一个任务
     * @param taskId 要删除的任务ID
     */
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据负责人ID，获取其所有未完成的任务列表
     * @param assigneeId 负责人的用户ID
     * @return 包含分配给该用户的所有Task对象的列表
     */
    public List<Task> getTasksByAssigneeId(int assigneeId) {
        List<Task> taskList = new ArrayList<>();
        // JOIN projects 表以获取项目名称
        String sql = "SELECT t.*, p.project_name FROM tasks t " +
                "JOIN projects p ON t.project_id = p.id " +
                "WHERE t.assignee_id = ? AND t.status != '已完成' ORDER BY t.due_date ASC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assigneeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt("id"));
                    task.setTaskName(rs.getString("task_name"));
                    task.setProjectId(rs.getInt("project_id"));
                    task.setAssigneeId(rs.getInt("assignee_id"));
                    task.setStatus(rs.getString("status"));
                    task.setDueDate(rs.getDate("due_date"));
                    task.setProjectName(rs.getString("project_name")); // 设置项目名称
                    taskList.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }
}