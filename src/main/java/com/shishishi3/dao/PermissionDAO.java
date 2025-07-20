package com.shishishi3.dao;

import com.shishishi3.model.Permission;
import com.shishishi3.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionDAO {

    /**
     * 获取系统中的所有权限列表
     * @return 包含所有Permission对象的列表
     */
    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT id, permission_name, description FROM permissions ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getInt("id"));
                p.setPermissionName(rs.getString("permission_name"));
                p.setDescription(rs.getString("description"));
                permissions.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * 获取指定角色拥有的所有权限的ID集合
     * @param roleId 要查询的角色ID
     * @return 包含权限ID的Set集合
     */
    public Set<Integer> getPermissionIdsForRole(int roleId) {
        Set<Integer> permissionIds = new HashSet<>();
        String sql = "SELECT permission_id FROM role_permissions WHERE role_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                permissionIds.add(rs.getInt("permission_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissionIds;
    }
}