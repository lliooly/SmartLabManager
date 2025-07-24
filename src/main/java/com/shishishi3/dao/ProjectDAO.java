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
     * 创建一个新项目。
     * @param project 包含新项目信息的对象
     */
    /**
     * 创建一个新项目。
     * @param project 包含新项目信息的对象
     */
    public void createProject(Project project) {
        String sql = "INSERT INTO projects (project_name, description, creator_id, start_date, end_date, purpose, procedure_steps, reagents_and_equipment, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setInt(3, project.getCreatorId());
            pstmt.setDate(4, project.getStartDate());
            pstmt.setDate(5, project.getEndDate());
            pstmt.setString(6, project.getPurpose());
            pstmt.setString(7, project.getProcedureSteps());
            pstmt.setString(8, project.getReagentsAndEquipment());
            pstmt.setInt(9, project.getStatusId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * 获取所有项目列表，并关联创建者名称和项目状态名称。
     * @return 包含所有项目的列表
     */
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        String sql = "SELECT p.*, u.full_name as creator_name, ps.status_name FROM projects p LEFT JOIN users u ON p.creator_id = u.id LEFT JOIN project_statuses ps ON p.status_id = ps.id ORDER BY p.created_at DESC";
        try (Connection conn = DbUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { projectList.add(mapRowToProject(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return projectList;
    }
    /**
     * 根据项目ID获取单个项目的详细信息。
     * @param projectId 项目ID
     * @return Project 对象，如果未找到则返回 null
     */
    /**
     * 【最小化测试版】根据项目ID获取单个项目的核心信息。
     * 这个版本只查询 projects 单张表，不进行任何JOIN操作，以定位问题。
     * @param projectId 项目ID
     * @return Project 对象，如果未找到则返回 null
     */
    public Project getProjectById(int projectId) {
        Project project = null;
        // 正确的SQL，它连接了必要的表以获取所有数据
        String sql = "SELECT p.*, u.full_name as creator_name, ps.status_name " +
                "FROM projects p " +
                "LEFT JOIN users u ON p.creator_id = u.id " +
                "LEFT JOIN project_statuses ps ON p.status_id = ps.id " +
                "WHERE p.id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 使用完整的、健壮的映射器
                    project = mapRowToProject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return project;
    }

    /**
     * 【最小化测试版】一个极简的辅助方法，只映射projects表的核心字段。
     */
    private Project mapRowToProject_Minimal(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setCreatorId(rs.getInt("creator_id"));
        project.setRiskAssessmentReport(rs.getString("risk_assessment_report"));
        // 注意：我们在这个测试版中，故意不去映射那些需要JOIN才能获得的字段，
        // 如 creatorName 和 statusName
        return project;
    }

    /**
     * 更新一个项目的状态。
     * @param projectId 要更新的项目ID
     * @param newStatusId 新的状态ID
     */
    public void updateProjectStatus(int projectId, int newStatusId) {
        String sql = "UPDATE projects SET status_id = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStatusId);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }


    /**
     * 根据创建者ID获取其创建的所有项目列表。
     * @param creatorId 创建者的用户ID
     * @return 该用户创建的所有项目的列表
     */
    public List<Project> getProjectsByCreatorId(int creatorId) {
        List<Project> projectList = new ArrayList<>();
        String sql = "SELECT p.*, u.full_name as creator_name, ps.status_name FROM projects p LEFT JOIN users u ON p.creator_id = u.id LEFT JOIN project_statuses ps ON p.status_id = ps.id WHERE p.creator_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, creatorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { projectList.add(mapRowToProject(rs)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return projectList;
    }

    /**
     * 更新项目的风险评估报告。
     * @param projectId 项目ID
     * @param reportJson 风险评估报告的JSON字符串
     */
    public void updateRiskAssessmentReport(int projectId, String reportJson) {
        String sql = "UPDATE projects SET risk_assessment_report = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reportJson);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * 将AI生成的风险评估报告保存到指定的项目中
     * @param projectId 项目ID
     * @param reportJson AI返回的JSON格式的报告字符串
     */
    public void saveAssessmentReport(int projectId, String reportJson) {
        String sql = "UPDATE projects SET risk_assessment_report = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reportJson);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * 统计不同状态的项目数量。
     * @return 一个Map，键是状态名称，值是对应的项目数量
     */
    public Map<String, Integer> getProjectStatusCounts() {
        Map<String, Integer> statusCounts = new HashMap<>();
        String sql = "SELECT ps.status_name, COUNT(p.id) as project_count FROM projects p JOIN project_statuses ps ON p.status_id = ps.id GROUP BY ps.status_name";
        try (Connection conn = DbUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { statusCounts.put(rs.getString("status_name"), rs.getInt("project_count")); }
        } catch (SQLException e) { e.printStackTrace(); }
        return statusCounts;
    }


    /**
     * 【核心修正】一个更健壮的辅助方法，用于将 ResultSet 的当前行映射到一个 Project 对象。
     * 在读取每一列前都会检查该列是否存在，防止因数据库列名与代码不一致而导致的崩溃。
     * @param rs SQL查询返回的 ResultSet
     * @return 填充好数据的 Project 对象
     * @throws SQLException
     */
    private Project mapRowToProject(ResultSet rs) throws SQLException {
        Project project = new Project();

        // 使用 hasColumn 对每一列进行防御性检查
        if (hasColumn(rs, "id")) project.setId(rs.getInt("id"));
        if (hasColumn(rs, "project_name")) project.setProjectName(rs.getString("project_name"));
        if (hasColumn(rs, "description")) project.setDescription(rs.getString("description"));
        if (hasColumn(rs, "creator_id")) project.setCreatorId(rs.getInt("creator_id"));
        if (hasColumn(rs, "start_date")) project.setStartDate(rs.getDate("start_date"));
        if (hasColumn(rs, "end_date")) project.setEndDate(rs.getDate("end_date"));
        if (hasColumn(rs, "created_at")) project.setCreatedAt(rs.getTimestamp("created_at"));
        if (hasColumn(rs, "purpose")) project.setPurpose(rs.getString("purpose"));
        if (hasColumn(rs, "procedure_steps")) project.setProcedureSteps(rs.getString("procedure_steps"));
        if (hasColumn(rs, "reagents_and_equipment")) project.setReagentsAndEquipment(rs.getString("reagents_and_equipment"));
        if (hasColumn(rs, "risk_assessment_report")) project.setRiskAssessmentReport(rs.getString("risk_assessment_report"));
        if (hasColumn(rs, "status_id")) project.setStatusId(rs.getInt("status_id"));

        // 设置从JOIN查询中获得的额外字段
        if (hasColumn(rs, "creator_name")) {
            project.setCreatorName(rs.getString("creator_name"));
        }
        if (hasColumn(rs, "status_name")) {
            project.setStatus(rs.getString("status_name"));
        }

        return project;
    }


    /**
     * 检查 ResultSet 是否包含某个列名，避免在某些简单查询中出错。
     * @param rs ResultSet 对象
     * @param columnName 要检查的列名
     * @return 如果存在则返回 true，否则返回 false
     */
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            // 预期中的异常，说明列不存在，返回false即可
            return false;
        }
    }
}