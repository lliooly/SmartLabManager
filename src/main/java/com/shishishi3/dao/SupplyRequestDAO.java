package com.shishishi3.dao;

import com.shishishi3.model.SupplyRequest;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyRequestDAO {

    /**
     * 创建一个新的物资申领记录
     * @param request 要创建的申领对象
     */
    public void createRequest(SupplyRequest request) {
        String sql = "INSERT INTO supply_requests (supply_id, requester_id, quantity_requested, notes, status) VALUES (?, ?, ?, ?, '待审批')";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getSupplyId());
            pstmt.setInt(2, request.getRequesterId());
            pstmt.setInt(3, request.getQuantityRequested());
            pstmt.setString(4, request.getNotes());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户ID，获取该用户的所有申领记录
     * @param userId 要查询的用户ID
     * @return 包含该用户所有申领记录的列表
     */
    public List<SupplyRequest> getRequestsByUserId(int userId) {
        List<SupplyRequest> requests = new ArrayList<>();
        // 使用JOIN查询，获取物资的名称
        String sql = "SELECT sr.*, s.name as supply_name FROM supply_requests sr " +
                "JOIN supplies s ON sr.supply_id = s.id " +
                "WHERE sr.requester_id = ? ORDER BY sr.request_date DESC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SupplyRequest req = new SupplyRequest();
                req.setId(rs.getInt("id"));
                req.setSupplyId(rs.getInt("supply_id"));
                req.setRequesterId(rs.getInt("requester_id"));
                req.setQuantityRequested(rs.getInt("quantity_requested"));
                req.setStatus(rs.getString("status"));
                req.setRequestDate(rs.getTimestamp("request_date"));
                req.setNotes(rs.getString("notes"));
                req.setSupplyName(rs.getString("supply_name")); // 设置物资名称
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * 获取所有状态为“待审批”的申领记录
     * @return 包含所有待审批申领记录的列表
     */
    public List<SupplyRequest> getAllPendingRequests() {
        List<SupplyRequest> requests = new ArrayList<>();
        // JOIN users 和 supplies 表以获取申领人和物资的名称
        String sql = "SELECT sr.*, s.name as supply_name, u.full_name as requester_name " +
                "FROM supply_requests sr " +
                "JOIN supplies s ON sr.supply_id = s.id " +
                "JOIN users u ON sr.requester_id = u.id " +
                "WHERE sr.status = '待审批' ORDER BY sr.request_date ASC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                SupplyRequest req = new SupplyRequest();
                // ... (省略与 getRequestsByUserId 中类似的字段设置)
                req.setSupplyName(rs.getString("supply_name"));
                req.setRequesterName(rs.getString("requester_name"));
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * 更新一个申领记录的状态
     * @param requestId  要更新的申领ID
     * @param newStatus  新的状态 ('已批准' 或 '已驳回')
     * @param approverId 执行审批操作的管理员ID
     */
    public void updateRequestStatus(int requestId, String newStatus, int approverId) {
        String sql = "UPDATE supply_requests SET status = ?, approver_id = ?, approval_date = NOW() WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, approverId);
            pstmt.setInt(3, requestId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}