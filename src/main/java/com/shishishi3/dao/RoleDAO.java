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
}