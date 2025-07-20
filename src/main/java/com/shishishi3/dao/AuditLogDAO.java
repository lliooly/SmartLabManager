package com.shishishi3.dao;

import com.shishishi3.util.DbUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogDAO {

    public void logAction(int operatorId, String operatorUsername, String action, String targetType, int targetId) {
        String sql = "INSERT INTO audit_log (operator_id, operator_username, action, target_type, target_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, operatorId);
            pstmt.setString(2, operatorUsername);
            pstmt.setString(3, action);
            pstmt.setString(4, targetType);
            pstmt.setInt(5, targetId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}