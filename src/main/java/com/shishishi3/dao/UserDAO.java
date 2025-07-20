package com.shishishi3.dao;

import com.shishishi3.model.User;
import com.shishishi3.util.DbUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDAO {

    /**
     * Authenticates a user and fetches all their permissions.
     * @param username The username submitted from the login form.
     * @param plainTextPassword The plain text password submitted from the login form.
     * @return A User object populated with permissions if login is successful, otherwise null.
     */
    public User loginAndGetPermissions(String username, String plainTextPassword) {
        String sql = "SELECT id, username, password, full_name, email FROM users WHERE username = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (BCrypt.checkpw(plainTextPassword, rs.getString("password"))) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPermissions(getUserPermissions(conn, user.getId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 私有辅助方法：获取指定用户的所有权限字符串。
     * (这个方法可能在您之前的 loginAndGetPermissions 方法中已经存在，
     * 如果不存在，请添加它。如果已存在，请确保它是 public 或 package-private 以便被复用)
     * @param conn 数据库连接
     * @param userId 用户ID
     * @return 权限字符串的Set集合
     * @throws SQLException
     */
    private Set<String> getUserPermissions(Connection conn, int userId) throws SQLException {
        Set<String> permissions = new HashSet<>();
        String sql = "SELECT DISTINCT p.permission_name FROM permissions p " +
                "JOIN role_permissions rp ON p.id = rp.permission_id " +
                "JOIN user_roles ur ON rp.role_id = ur.role_id " +
                "WHERE ur.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                permissions.add(rs.getString("permission_name"));
            }
        }
        return permissions;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, username, full_name, email FROM users";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 根据用户ID获取单个用户的基础信息（不含权限）。
     * @param userId 要查询的用户ID。
     * @return User对象，如果未找到则返回null。
     */
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT id, username, full_name, email FROM users WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 更新指定用户的角色分配。
     * 此方法使用数据库事务来确保操作的原子性：要么全部成功，要么全部失败。
     * @param userId 要更新的用户的ID。
     * @param roleIds 一个包含新角色ID的字符串数组。
     */
    public void updateUserRoles(int userId, String[] roleIds) {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            // 步骤 1: 开启事务，禁止自动提交
            conn.setAutoCommit(false);

            // 步骤 2: 删除该用户所有旧的角色关系
            String deleteSql = "DELETE FROM user_roles WHERE user_id = ?";
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
                deletePstmt.setInt(1, userId);
                deletePstmt.executeUpdate();
            }

            // 步骤 3: 插入用户新的角色关系
            if (roleIds != null && roleIds.length > 0) {
                String insertSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    for (String roleIdStr : roleIds) {
                        insertPstmt.setInt(1, userId);
                        insertPstmt.setInt(2, Integer.parseInt(roleIdStr));
                        insertPstmt.addBatch(); // 添加到批处理
                    }
                    insertPstmt.executeBatch(); // 执行批处理插入
                }
            }

            // 步骤 4: 如果一切顺利，提交事务
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            // 步骤 5: 如果发生任何错误，回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 步骤 6: 恢复自动提交模式并关闭连接
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 注册一个新用户，并为其分配一个默认角色。
     * @param user 包含新用户信息（用户名, 哈希后的密码, 邮箱等）的对象。
     * @param defaultRoleId 要分配给新用户的默认角色的ID (例如: 3 代表 'Student')。
     * @return 如果注册成功返回true，如果用户名或邮箱已存在则返回false。
     */
    public boolean registerUser(User user, int defaultRoleId) {
        String checkSql = "SELECT id FROM users WHERE username = ? OR email = ?";
        String insertUserSql = "INSERT INTO users (username, password, full_name, email) VALUES (?, ?, ?, ?)";
        String insertRoleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();

            // 检查用户是否存在
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setString(1, user.getUsername());
                checkPstmt.setString(2, user.getEmail());
                if (checkPstmt.executeQuery().next()) return false; // 用户已存在
            }

            conn.setAutoCommit(false); // 开始事务

            long newUserId;
            // 插入用户
            try (PreparedStatement userPstmt = conn.prepareStatement(insertUserSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userPstmt.setString(1, user.getUsername());
                userPstmt.setString(2, user.getPassword());
                userPstmt.setString(3, user.getFullName());
                userPstmt.setString(4, user.getEmail());
                userPstmt.executeUpdate();
                ResultSet rs = userPstmt.getGeneratedKeys();
                if (rs.next()) newUserId = rs.getLong(1);
                else throw new SQLException("Creating user failed, no ID obtained.");
            }

            // 分配角色
            try (PreparedStatement rolePstmt = conn.prepareStatement(insertRoleSql)) {
                rolePstmt.setLong(1, newUserId);
                rolePstmt.setInt(2, defaultRoleId);
                rolePstmt.executeUpdate();
            }

            conn.commit(); // 提交事务
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean isUserExists(String username, String email) {
        String sql = "SELECT id FROM users WHERE username = ? OR email = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 如果能找到记录，返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * A utility method to help you create hashed passwords to insert into your database.
     * Run this once to generate hashes for your test users.
     */
    public static void main(String[] args) {
        String plainPassword1 = "admin123";
        String plainPassword2 = "teacher123";
        String plainPassword3 = "student123";

        String hashedPassword1 = BCrypt.hashpw(plainPassword1, BCrypt.gensalt());
        String hashedPassword2 = BCrypt.hashpw(plainPassword2, BCrypt.gensalt());
        String hashedPassword3 = BCrypt.hashpw(plainPassword3, BCrypt.gensalt());

        System.out.println("Admin Hash: " + hashedPassword1);
        System.out.println("Teacher Hash: " + hashedPassword2);
        System.out.println("Student Hash: " + hashedPassword3);

        // Example: To insert an admin user, you would run an SQL statement like:
        // INSERT INTO users (username, password, email, full_name) VALUES ('admin', '$2a$10$....', 'admin@example.com', 'Administrator');
    }


}