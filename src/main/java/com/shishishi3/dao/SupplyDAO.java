package com.shishishi3.dao;

import com.shishishi3.model.Supply;
import com.shishishi3.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplyDAO {

    /**
     * 获取所有物资的列表，按名称排序
     * @return 包含所有 Supply 对象的列表
     */
    public List<Supply> getAllSupplies() {
        List<Supply> supplyList = new ArrayList<>();
        String sql = "SELECT * FROM supplies ORDER BY name";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Supply supply = new Supply();
                supply.setId(rs.getInt("id"));
                supply.setName(rs.getString("name"));
                supply.setDescription(rs.getString("description"));
                supply.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                supply.setReorderLevel(rs.getInt("reorder_level"));
                supply.setUnit(rs.getString("unit"));
                supplyList.add(supply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplyList;
    }

    /**
     * 添加新的物资到库存 (供管理员/教师使用)
     * @param supply 要添加的物资对象
     */
    public void addSupply(Supply supply) {
        String sql = "INSERT INTO supplies (name, description, quantity_on_hand, reorder_level, unit) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supply.getName());
            pstmt.setString(2, supply.getDescription());
            pstmt.setInt(3, supply.getQuantityOnHand());
            pstmt.setInt(4, supply.getReorderLevel());
            pstmt.setString(5, supply.getUnit());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新指定物资的库存数量
     * @param supplyId 要更新的物资ID
     * @param newQuantity 新的库存数量
     */
    public void updateSupplyQuantity(int supplyId, int newQuantity) {
        String sql = "UPDATE supplies SET quantity_on_hand = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, supplyId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据物资ID查询物资详情
     * @param supplyId 要查询的物资ID
     * @return 如果找到，则返回Supply对象；否则返回null
     */
    public Supply getSupplyById(int supplyId) {
        Supply supply = null;
        String sql = "SELECT * FROM supplies WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    supply = new Supply();
                    supply.setId(rs.getInt("id"));
                    supply.setName(rs.getString("name"));
                    // 假设您的Supply模型有description字段
                    // supply.setDescription(rs.getString("description"));
                    supply.setQuantityOnHand(rs.getInt("quantity_on_hand"));
                    // 假设您的Supply模型有reorderLevel和unit字段
                    // supply.setReorderLevel(rs.getInt("reorder_level"));
                    // supply.setUnit(rs.getString("unit"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supply;
    }

    /**
     * 更新指定物资的库存数量
     * @param supplyId 要更新的物资ID
     * @param newQuantity 新的库存数量
     * @return 如果更新成功，返回true；否则返回false
     */
    public boolean updateStockQuantity(int supplyId, int newQuantity) {
        String sql = "UPDATE supplies SET quantity_on_hand = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, supplyId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 如果影响的行数大于0，说明更新成功

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}