package com.shishishi3.model;

import java.sql.Timestamp;

public class VenueBooking {

    private int id;
    private int venueId;
    private int userId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String purpose;
    private String status; // '已预约', '已取消', '已完成'

    // 非数据库字段，用于方便在JSP中显示关联数据
    private String venueName;
    private String userName;

    public VenueBooking() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 非数据库字段的 Getters/Setters
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}