package com.shishishi3.dao;

import com.shishishi3.model.Role;
import com.shishishi3.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleDAO {

    /**
     * 从数据库中获取所有角色的列表。
     * @return 包含所有 Role 对象的列表。
     */
    public List<Role> getAllRoles() {
        List<Role> allRoles = new ArrayList<>();
        String sql = "SELECT id, role_name FROM roles ORDER BY id";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                allRoles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 在真实应用中，这里应该有更完善的异常处理
        }
        return allRoles;
    }

    /**
     * 根据用户ID，获取该用户拥有的所有角色的ID集合。
     * @param userId 要查询的用户ID。
     * @return 包含角色ID的Set集合。
     */
    public Set<Integer> getRoleIdsForUser(int userId) {
        Set<Integer> currentUserRoleIds = new HashSet<>();
        String sql = "SELECT role_id FROM user_roles WHERE user_id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    currentUserRoleIds.add(rs.getInt("role_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentUserRoleIds;
    }
    /**
     * 根据ID获取单个角色的信息
     * @param roleId 角色ID
     * @return Role对象，未找到则返回null
     */
    public Role getRoleById(int roleId) {
        Role role = null;
        String sql = "SELECT id, role_name, description FROM roles WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    public Set<Role> getRolesByUserId(int userId) {
        Set<Role> roles = new HashSet<>();
        String sql = "SELECT r.id, r.role_name, r.description FROM roles r " +
                "JOIN user_roles ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 更新指定角色的权限分配（使用事务）
     * @param roleId 要更新的角色ID
     * @param permissionIds 包含新权限ID的字符串数组
     */
    public void updateRolePermissions(int roleId, String[] permissionIds) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 删除该角色所有旧的权限关系
            String deleteSql = "DELETE FROM role_permissions WHERE role_id = ?";
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                deletePstmt.setInt(1, roleId);
                deletePstmt.executeUpdate();
            }

            // 2. 插入该角色新的权限关系
            if (permissionIds != null && permissionIds.length > 0) {
                String insertSql = "INSERT INTO role_permissions (role_id, permission_id) VALUES (?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    for (String permissionIdStr : permissionIds) {
                        insertPstmt.setInt(1, roleId);
                        insertPstmt.setInt(2, Integer.parseInt(permissionIdStr));
                        insertPstmt.addBatch();
                    }
                    insertPstmt.executeBatch();
                }
            }
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}