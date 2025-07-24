package com.shishishi3.dao;

import com.shishishi3.model.Equipment;
import com.shishishi3.model.EquipmentBooking;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EquipmentDAO {

    /**
     * 获取所有设备的列表
     */
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipment ORDER BY name";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                equipmentList.add(mapRowToEquipment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    /**
     * 根据ID获取单个设备的详细信息 (修正版)
     * @param id 设备ID
     * @return Equipment对象，未找到则返回null
     */
    public Equipment getEquipmentById(int id) {
        Equipment equipment = null;
        String sql = "SELECT * FROM equipment WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 使用辅助方法来填充对象，确保所有字段都被正确赋值
                equipment = mapRowToEquipment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipment;
    }


    /**
     * 添加一个新设备到数据库
     */
    public void addEquipment(Equipment equipment) {
        String sql = "INSERT INTO equipment (name, model, serial_number, status, location, purchase_date, last_maintenance_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipment.getName());
            pstmt.setString(2, equipment.getModel());
            pstmt.setString(3, equipment.getSerialNumber());
            pstmt.setString(4, equipment.getStatus());
            pstmt.setString(5, equipment.getLocation());
            pstmt.setDate(6, equipment.getPurchaseDate());
            pstmt.setDate(7, equipment.getLastMaintenanceDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个新的设备预约记录
     * @throws SQLException
     */
    public void createBooking(EquipmentBooking booking) throws SQLException {
        String sql = "INSERT INTO equipment_bookings (equipment_id, user_id, start_time, end_time, purpose, status) VALUES (?, ?, ?, ?, ?, '已预约')";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getEquipmentId());
            pstmt.setInt(2, booking.getUserId());
            pstmt.setTimestamp(3, booking.getStartTime());
            pstmt.setTimestamp(4, booking.getEndTime());
            pstmt.setString(5, booking.getPurpose());
            pstmt.executeUpdate();
        }
    }

    /**
     * 检查指定设备在某个时间段内是否已被预约
     */
    public boolean hasBookingConflict(int equipmentId, Timestamp startTime, Timestamp endTime) {
        String sql = "SELECT id FROM equipment_bookings WHERE equipment_id = ? AND status = '已预约' AND (start_time < ? AND end_time > ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
            pstmt.setTimestamp(2, endTime);
            pstmt.setTimestamp(3, startTime);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据设备ID，获取该设备的所有预约记录
     *
     * @param equipmentId 要查询的设备ID
     * @return 包含该设备所有 EquipmentBooking 对象的列表
     */
    public List<EquipmentBooking> getBookingsForEquipment(int equipmentId) {
        List<EquipmentBooking> bookings = new ArrayList<>();
        // 使用JOIN查询，顺便获取预约人的姓名，方便显示
        String sql = "SELECT b.*, u.full_name FROM equipment_bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "WHERE b.equipment_id = ? AND b.status = '已预约' ORDER BY b.start_time ASC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EquipmentBooking booking = new EquipmentBooking();
                booking.setId(rs.getInt("id"));
                booking.setEquipmentId(rs.getInt("equipment_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setStartTime(rs.getTimestamp("start_time"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setPurpose(rs.getString("purpose"));
                booking.setStatus(rs.getString("status"));
                booking.setUserName(rs.getString("full_name")); // 设置预约人姓名
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * 根据ID获取单个预约的详细信息
     */
    public EquipmentBooking getBookingById(int bookingId) {
        EquipmentBooking booking = null;
        String sql = "SELECT * FROM equipment_bookings WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                booking = new EquipmentBooking();
                booking.setId(rs.getInt("id"));
                booking.setEquipmentId(rs.getInt("equipment_id"));
                // ... (省略其他字段的set)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    /**
     * 更新设备的状态
     */
    public void updateEquipmentStatus(int equipmentId, String newStatus) {
        String sql = "UPDATE equipment SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, equipmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新预约记录的状态
     */
    public void updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE equipment_bookings SET status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户ID，获取该用户的所有预约记录
     * @param userId 要查询的用户ID
     * @return 包含该用户所有 EquipmentBooking 对象的列表
     */
    public List<EquipmentBooking> getBookingsForUser(int userId) {
        List<EquipmentBooking> bookings = new ArrayList<>();
        // 使用JOIN查询，顺便获取设备名称
        String sql = "SELECT b.*, e.name as equipment_name FROM equipment_bookings b " +
                "JOIN equipment e ON b.equipment_id = e.id " +
                "WHERE b.user_id = ? ORDER BY b.start_time DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                EquipmentBooking booking = new EquipmentBooking();
                booking.setId(rs.getInt("id"));
                booking.setEquipmentId(rs.getInt("equipment_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setStartTime(rs.getTimestamp("start_time"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setStatus(rs.getString("status"));
                booking.setEquipmentName(rs.getString("equipment_name")); // 设置设备名称
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * 更新设备的维保信息（状态和上次维保日期）
     * @param equipmentId 要更新的设备ID
     * @param newMaintDate 新的维保日期
     * @param newStatus 新的状态
     */
    public void updateMaintenanceInfo(int equipmentId, Date newMaintDate, String newStatus) {
        String sql = "UPDATE equipment SET last_maintenance_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, newMaintDate);
            pstmt.setString(2, newStatus);
            pstmt.setInt(3, equipmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getMonthlyBookingCounts() {
        Map<String, Integer> monthlyCounts = new LinkedHashMap<>(); // 使用LinkedHashMap保持月份顺序
        String sql = "SELECT DATE_FORMAT(start_time, '%Y-%m') as month, COUNT(*) as count " +
                "FROM equipment_bookings " +
                "WHERE start_time >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
                "GROUP BY month ORDER BY month ASC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                monthlyCounts.put(rs.getString("month"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyCounts;
    }

    /**
     * 私有辅助方法，用于将ResultSet的一行映射到一个Equipment对象，避免代码重复
     */
    private Equipment mapRowToEquipment(ResultSet rs) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setId(rs.getInt("id"));
        equipment.setName(rs.getString("name"));
        equipment.setModel(rs.getString("model"));
        equipment.setSerialNumber(rs.getString("serial_number"));
        equipment.setStatus(rs.getString("status"));
        equipment.setLocation(rs.getString("location"));
        equipment.setPurchaseDate(rs.getDate("purchase_date"));
        equipment.setLastMaintenanceDate(rs.getDate("last_maintenance_date"));
        return equipment;
    }
}