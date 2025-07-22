package com.shishishi3.dao;

import com.shishishi3.model.AuditLog;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    /**
     * 记录一条操作日志 (已更新，包含ipAddress)
     */
    public void logAction(int operatorId, String operatorUsername, String action, String targetType, int targetId, String ipAddress) {
        String sql = "INSERT INTO audit_log (operator_id, operator_username, action, target_type, target_id, ip_address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, operatorId);
            pstmt.setString(2, operatorUsername);
            pstmt.setString(3, action);
            pstmt.setString(4, targetType);
            pstmt.setInt(5, targetId);
            pstmt.setString(6, ipAddress);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据条件搜索审计日志
     */
    public List<AuditLog> searchLogs(String username, Date startDate, Date endDate) {
        List<AuditLog> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM audit_log WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (username != null && !username.trim().isEmpty()) {
            sql.append(" AND operator_username LIKE ?");
            params.add("%" + username.trim() + "%");
        }
        if (startDate != null) {
            sql.append(" AND action_timestamp >= ?");
            params.add(Timestamp.valueOf(startDate + " 00:00:00"));
        }
        if (endDate != null) {
            sql.append(" AND action_timestamp <= ?");
            params.add(Timestamp.valueOf(endDate + " 23:59:59"));
        }
        sql.append(" ORDER BY action_timestamp DESC LIMIT 100");

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setId(rs.getInt("id"));
                log.setOperatorId(rs.getInt("operator_id"));
                log.setOperatorUsername(rs.getString("operator_username"));
                log.setAction(rs.getString("action"));
                log.setTargetType(rs.getString("target_type"));
                log.setTargetId(rs.getInt("target_id"));
                log.setActionTimestamp(rs.getTimestamp("action_timestamp"));
                log.setIpAddress(rs.getString("ip_address"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}