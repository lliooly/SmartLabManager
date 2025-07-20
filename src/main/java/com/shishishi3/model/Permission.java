package com.shishishi3.model;

public class Permission {
    private int id;
    private String permissionName;
    private String description;

    // Getters and Setters...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}