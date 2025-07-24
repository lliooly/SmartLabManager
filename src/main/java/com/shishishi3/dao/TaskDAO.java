package com.shishishi3.dao;

import com.shishishi3.model.Task;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /**
     * 根据项目ID，获取该项目下的所有任务
     *
     * @param projectId 项目ID
     * @return 任务列表
     */
    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> taskList = new ArrayList<>();
        // SQL语句加入了对 task_statuses 表的 JOIN
        String sql = "SELECT t.*, u.full_name as assignee_name, ts.status_name FROM tasks t " +
                "LEFT JOIN users u ON t.assignee_id = u.id " +
                "LEFT JOIN task_statuses ts ON t.status_id = ts.id " +
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
                task.setStatusId(rs.getInt("status_id"));
                task.setStatusName(rs.getString("status_name"));
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
     *
     * @param task 包含新任务信息的对象
     */
    public void createTask(Task task) {
        // 修正了SQL语句，改为向 'status_id' 列插入数据
        String sql = "INSERT INTO tasks (task_name, description, project_id, assignee_id, priority, status_id, due_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getProjectId());
            if (task.getAssigneeId() == 0) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, task.getAssigneeId());
            }
            pstmt.setString(5, task.getPriority());
            // 使用 getStatusId() 而非不存在的 getStatus()
            pstmt.setInt(6, task.getStatusId());
            pstmt.setDate(7, task.getDueDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新一个任务的状态
     *
     * @param taskId 要更新的任务ID
     */
    public void updateTaskStatus(int taskId, int statusId) {
        String sql = "UPDATE tasks SET status_id = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, statusId);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID删除一个任务
     *
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
     *
     * @param assigneeId 负责人的用户ID
     * @return 包含分配给该用户的所有Task对象的列表
     */
    public List<Task> getTasksByAssigneeId(int assigneeId) {
        List<Task> taskList = new ArrayList<>();
        // 修正了SQL：JOIN task_statuses 表以通过状态名称进行过滤，并查询状态相关字段
        String sql = "SELECT t.*, p.project_name, ts.status_name FROM tasks t " +
                "JOIN projects p ON t.project_id = p.id " +
                "JOIN task_statuses ts ON t.status_id = ts.id " +
                "WHERE t.assignee_id = ? AND ts.status_name != '已完成' ORDER BY t.due_date ASC";

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
                    // 正确地从查询结果中设置 statusId 和 statusName
                    task.setStatusId(rs.getInt("status_id"));
                    task.setStatusName(rs.getString("status_name"));
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
