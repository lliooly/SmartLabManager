package com.shishishi3.model;

import java.util.Set;

public class User {
    private int id;
    private String username;
    private String password; // 只在注册和密码修改时临时使用
    private String fullName;
    private String email;
    private Set<String> permissions; // 存储用户最终拥有的所有权限字符串
    private Set<Role> roles;         // 存储用户拥有的角色对象

    // 默认构造函数
    public User() {}
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters for all fields...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // 权限检查的辅助方法
    public boolean hasPermission(String permissionName) {
        return this.permissions != null && this.permissions.contains(permissionName);
    }
}