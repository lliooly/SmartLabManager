package com.shishishi3.model;

import java.sql.Timestamp;

public class EquipmentBooking {

    private int id;
    private int equipmentId;
    private int userId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String purpose;
    private String status; // '已预约', '已取消', '已完成'

    // 为了方便在JSP中显示，我们可以额外加入一些非数据库字段
    private String equipmentName;
    private String userName;

    // --- 默认构造函数 ---
    public EquipmentBooking() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

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
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}