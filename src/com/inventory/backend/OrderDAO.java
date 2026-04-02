package com.inventory.backend;

import com.inventory.model.OrderBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC: Full CRUD for orders table.
 * Runtime fix: null-check connection before every operation.
 */
public class OrderDAO {

    private Connection conn() throws SQLException {
        Connection c = DBConnection.getConnection();
        if (c == null) throw new SQLException("No database connection available.");
        return c;
    }

    public boolean addOrder(OrderBean o) {
        String sql = "INSERT INTO orders(product_id,supplier_id,quantity,total_amount,order_date,status) VALUES(?,?,?,?,CURDATE(),?)";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setInt   (1, o.getProductId());
            ps.setInt   (2, o.getSupplierId());
            ps.setInt   (3, o.getQuantity());
            ps.setDouble(4, o.getTotalAmount());
            ps.setString(5, o.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] addOrder: " + e.getMessage());
            return false;
        }
    }

    public List<OrderBean> getAllOrders() {
        List<OrderBean> list = new ArrayList<>();
        String sql = "SELECT o.*, p.product_name, s.supplier_name " +
                     "FROM orders o " +
                     "JOIN products  p ON o.product_id  = p.product_id " +
                     "JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                     "ORDER BY o.order_id DESC";
        try {
            ResultSet rs = conn().createStatement().executeQuery(sql);
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getAllOrders: " + e.getMessage());
        }
        return list;
    }

    public List<OrderBean> getOrdersByStatus(String status) {
        List<OrderBean> list = new ArrayList<>();
        String sql = "SELECT o.*, p.product_name, s.supplier_name " +
                     "FROM orders o " +
                     "JOIN products  p ON o.product_id  = p.product_id " +
                     "JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                     "WHERE o.status = ?";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO] getOrdersByStatus: " + e.getMessage());
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String status) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "UPDATE orders SET status=? WHERE order_id=?");
            ps.setString(1, status);
            ps.setInt   (2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] updateStatus: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteOrder(int orderId) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM orders WHERE order_id=?");
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrderDAO] deleteOrder: " + e.getMessage());
            return false;
        }
    }

    public int getActiveOrderCount() {
        try {
            ResultSet rs = conn().createStatement().executeQuery(
                "SELECT COUNT(*) FROM orders WHERE status NOT IN ('DELIVERED','CANCELLED')");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[OrderDAO] count: " + e.getMessage());
        }
        return 0;
    }
private OrderBean mapRow(ResultSet rs) throws SQLException {
        return new OrderBean(
            rs.getInt   ("order_id"),
            rs.getInt   ("product_id"),
            rs.getString("product_name"),
            rs.getInt   ("supplier_id"),
            rs.getString("supplier_name"),
            rs.getInt   ("quantity"),
            rs.getDouble("total_amount"),
            rs.getString("order_date"),
            rs.getString("status")
        );
    }
}
