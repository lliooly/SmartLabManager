package com.shishishi3.model;

import java.sql.Timestamp;

public class AuditLog {
    private int id;
    private int operatorId;
    private String operatorUsername;
    private String action;
    private String targetType;
    private int targetId;
    private Timestamp actionTimestamp;
    private String ipAddress;

    public AuditLog() {}

    // --- Getters and Setters for all fields ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOperatorId() { return operatorId; }
    public void setOperatorId(int operatorId) { this.operatorId = operatorId; }
    public String getOperatorUsername() { return operatorUsername; }
    public void setOperatorUsername(String operatorUsername) { this.operatorUsername = operatorUsername; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }
    public Timestamp getActionTimestamp() { return actionTimestamp; }
    public void setActionTimestamp(Timestamp actionTimestamp) { this.actionTimestamp = actionTimestamp; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}