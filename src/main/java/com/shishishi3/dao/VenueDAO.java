package com.shishishi3.dao;

import com.shishishi3.model.Venue;
import com.shishishi3.model.VenueBooking;
import com.shishishi3.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    /**
     * 获取所有场地的列表
     * @return 包含所有 Venue 对象的列表
     */
    public List<Venue> getAllVenues() {
        List<Venue> venueList = new ArrayList<>();
        String sql = "SELECT * FROM venues WHERE status = '可用' ORDER BY name";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Venue venue = new Venue();
                venue.setId(rs.getInt("id"));
                venue.setName(rs.getString("name"));
                venue.setDescription(rs.getString("description"));
                venue.setCapacity(rs.getInt("capacity"));
                venue.setLocation(rs.getString("location"));
                venue.setStatus(rs.getString("status"));
                venueList.add(venue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venueList;
    }

    /**
     * 根据ID获取单个场地的详细信息
     * @param venueId 场地ID
     * @return Venue对象，未找到则返回null
     */
    public Venue getVenueById(int venueId) {
        Venue venue = null;
        String sql = "SELECT * FROM venues WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                venue = new Venue();
                venue.setId(rs.getInt("id"));
                venue.setName(rs.getString("name"));
                venue.setDescription(rs.getString("description"));
                venue.setCapacity(rs.getInt("capacity"));
                venue.setLocation(rs.getString("location"));
                venue.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venue;
    }

    /**
     * 获取指定场地的所有预约记录
     * @param venueId 场地ID
     * @return 该场地的预约记录列表
     */
    public List<VenueBooking> getBookingsForVenue(int venueId) {
        List<VenueBooking> bookings = new ArrayList<>();
        String sql = "SELECT vb.*, u.full_name FROM venue_bookings vb " +
                "JOIN users u ON vb.user_id = u.id " +
                "WHERE vb.venue_id = ? AND vb.status = '已预约' ORDER BY vb.start_time ASC";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                VenueBooking booking = new VenueBooking();
                booking.setId(rs.getInt("id"));
                booking.setVenueId(rs.getInt("venue_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setStartTime(rs.getTimestamp("start_time"));
                booking.setEndTime(rs.getTimestamp("end_time"));
                booking.setPurpose(rs.getString("purpose"));
                booking.setStatus(rs.getString("status"));
                booking.setUserName(rs.getString("full_name"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * 检查指定场地在某个时间段内是否已被预约，防止冲突
     * @return 如果存在时间冲突则返回true，否则返回false
     */
    public boolean hasBookingConflict(int venueId, Timestamp startTime, Timestamp endTime) {
        String sql = "SELECT id FROM venue_bookings WHERE venue_id = ? AND status = '已预约' AND " +
                "(start_time < ? AND end_time > ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);

            // 检查新预约的结束时间是否在某个已有预约的中间
            pstmt.setTimestamp(2, endTime);
            // 检查新预约的开始时间是否在某个已有预约的中间
            pstmt.setTimestamp(3, startTime);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 如果能查到记录，说明存在冲突
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // 出错时默认有冲突，防止意外预定
        }
    }

    /**
     * 创建一个新的场地预约记录
     * @param booking 要创建的预约对象
     */
    public void createBooking(VenueBooking booking) {
        String sql = "INSERT INTO venue_bookings (venue_id, user_id, start_time, end_time, purpose, status) VALUES (?, ?, ?, ?, ?, '已预约')";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getVenueId());
            pstmt.setInt(2, booking.getUserId());
            pstmt.setTimestamp(3, booking.getStartTime());
            pstmt.setTimestamp(4, booking.getEndTime());
            pstmt.setString(5, booking.getPurpose());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个新的场地到数据库
     * @param venue 要添加的场地对象
     */
    public void addVenue(Venue venue) {
        String sql = "INSERT INTO venues (name, description, capacity, location, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getName());
            pstmt.setString(2, venue.getDescription());
            pstmt.setInt(3, venue.getCapacity());
            pstmt.setString(4, venue.getLocation());
            pstmt.setString(5, venue.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据用户ID，获取其所有未过期的场地预约记录
     * @param userId 预约者的用户ID
     * @return 包含该用户所有VenueBooking对象的列表
     */
    public List<VenueBooking> getBookingsByUserId(int userId) {
        List<VenueBooking> bookingList = new ArrayList<>();
        // JOIN venues 表以获取场地名称，并只查询未结束的预约
        String sql = "SELECT vb.*, v.name as venue_name FROM venue_bookings vb " +
                "JOIN venues v ON vb.venue_id = v.id " +
                "WHERE vb.user_id = ? AND vb.status = '已预约' AND vb.end_time > NOW() ORDER BY vb.start_time ASC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    VenueBooking booking = new VenueBooking();
                    booking.setId(rs.getInt("id"));
                    booking.setVenueId(rs.getInt("venue_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setStartTime(rs.getTimestamp("start_time"));
                    booking.setEndTime(rs.getTimestamp("end_time"));
                    booking.setStatus(rs.getString("status"));
                    booking.setVenueName(rs.getString("venue_name"));
                    bookingList.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }
}