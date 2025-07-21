package com.shishishi3.model;

import java.sql.Date;

public class Equipment {

    private int id;
    private String name;
    private String model;
    private String serialNumber;
    private String status; // '可用', '使用中', '维修中', '已报废'
    private String location;
    private Date purchaseDate;
    private Date lastMaintenanceDate;

    // --- 默认构造函数 ---
    public Equipment() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public Date getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(Date lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
}