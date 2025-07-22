package com.shishishi3.dao;

import com.shishishi3.model.Project;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDAO {

    /**
     * 创建一个新的实验项目（已更新，包含详细方案）
     * @param project 包含新项目信息的对象
     */
    public void createProject(Project project) {
        String sql = "INSERT INTO projects (project_name, description, status, creator_id, start_date, end_date, purpose, procedure_steps, reagents_and_equipment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setString(3, project.getStatus());
            pstmt.setInt(4, project.getCreatorId());
            pstmt.setDate(5, project.getStartDate());
            pstmt.setDate(6, project.getEndDate());

            // 为新增的字段设置参数
            pstmt.setString(7, project.getPurpose());
            pstmt.setString(8, project.getProcedureSteps());
            pstmt.setString(9, project.getReagentsAndEquipment());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有实验项目的列表
     * @return 包含所有 Project 对象的列表
     */
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        // 使用 LEFT JOIN，这样即使项目的创建者(creator)被删除，项目信息依然可以被查询出来
        String sql = "SELECT p.*, u.full_name as creator_name FROM projects p " +
                "LEFT JOIN users u ON p.creator_id = u.id " +
                "ORDER BY p.created_at DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setDescription(rs.getString("description"));
                project.setStatus(rs.getString("status"));
                project.setCreatorId(rs.getInt("creator_id"));
                project.setStartDate(rs.getDate("start_date"));
                project.setEndDate(rs.getDate("end_date"));
                project.setCreatedAt(rs.getTimestamp("created_at"));
                project.setCreatorName(rs.getString("creator_name")); // 设置创建者姓名
                projectList.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    public Project getProjectById(int projectId) {
        Project project = null;
        // 确保SELECT语句包含了所有新字段
        String sql = "SELECT p.*, u.full_name as creator_name FROM projects p " +
                "LEFT JOIN users u ON p.creator_id = u.id WHERE p.id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                project = new Project();
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setDescription(rs.getString("description"));
                project.setStatus(rs.getString("status"));
                project.setCreatorId(rs.getInt("creator_id"));
                project.setStartDate(rs.getDate("start_date"));
                project.setEndDate(rs.getDate("end_date"));
                project.setCreatedAt(rs.getTimestamp("created_at"));
                project.setCreatorName(rs.getString("creator_name"));
                // 获取新增的详细信息字段
                project.setPurpose(rs.getString("purpose"));
                project.setProcedureSteps(rs.getString("procedure_steps"));
                project.setReagentsAndEquipment(rs.getString("reagents_and_equipment"));
                project.setRiskAssessmentReport(rs.getString("risk_assessment_report"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    /**
     * 更新一个项目的状态
     * @param projectId 要更新的项目ID
     * @param newStatus 新的状态字符串
     */
    public void updateProjectStatus(int projectId, String newStatus) {
        String sql = "UPDATE projects SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据创建者ID，获取其创建的所有项目列表
     * @param userId 创建者的用户ID
     * @return 包含该用户所创建的所有Project对象的列表
     */
    public List<Project> getProjectsByCreatorId(int userId) {
        List<Project> projectList = new ArrayList<>();
        String sql = "SELECT p.*, u.full_name as creator_name FROM projects p " +
                "LEFT JOIN users u ON p.creator_id = u.id " +
                "WHERE p.creator_id = ? ORDER BY p.created_at DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setStatus(rs.getString("status"));
                    project.setCreatorId(rs.getInt("creator_id"));
                    project.setStartDate(rs.getDate("start_date"));
                    project.setEndDate(rs.getDate("end_date"));
                    project.setCreatedAt(rs.getTimestamp("created_at"));
                    project.setCreatorName(rs.getString("creator_name"));
                    projectList.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * 将AI生成的风险评估报告保存到指定的项目中
     * @param projectId 项目ID
     * @param reportJson AI返回的JSON格式的报告字符串
     */
    public void saveAssessmentReport(int projectId, String reportJson) {
        String sql = "UPDATE projects SET risk_assessment_report = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reportJson);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getProjectStatusCounts() {
        Map<String, Integer> statusCounts = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM projects GROUP BY status";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                statusCounts.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusCounts;
    }
}