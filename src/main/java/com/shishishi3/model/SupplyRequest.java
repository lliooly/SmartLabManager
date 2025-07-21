package com.shishishi3.model;

import java.sql.Timestamp;

public class SupplyRequest {
    private int id;
    private int supplyId;
    private int requesterId;
    private int quantityRequested;
    private String status;
    private Timestamp requestDate;
    private String notes;

    // 非数据库字段，用于方便在JSP中显示
    private String supplyName;
    private String requesterName;

    public SupplyRequest() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSupplyId() { return supplyId; }
    public void setSupplyId(int supplyId) { this.supplyId = supplyId; }
    public int getRequesterId() { return requesterId; }
    public void setRequesterId(int requesterId) { this.requesterId = requesterId; }
    public int getQuantityRequested() { return quantityRequested; }
    public void setQuantityRequested(int quantityRequested) { this.quantityRequested = quantityRequested; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getSupplyName() { return supplyName; }
    public void setSupplyName(String supplyName) { this.supplyName = supplyName; }
    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }
}