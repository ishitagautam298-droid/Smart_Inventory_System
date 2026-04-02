package com.inventory.backend;

import com.inventory.model.ProductBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC: Full CRUD for products table.
 * Runtime fix: null-check connection before every operation.
 */
public class ProductDAO {

    // Guard method — called at top of every DAO method
    private Connection conn() throws SQLException {
        Connection c = DBConnection.getConnection();
        if (c == null) throw new SQLException("No database connection available.");
        return c;
    }

    public boolean addProduct(ProductBean p) {
        String sql = "INSERT INTO products(product_name,category,quantity,price,reorder_level) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getCategory());
            ps.setInt   (3, p.getQuantity());
            ps.setDouble(4, p.getPrice());
            ps.setInt   (5, p.getReorderLevel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] addProduct: " + e.getMessage());
            return false;
        }
    }

    public List<ProductBean> getAllProducts() {
        List<ProductBean> list = new ArrayList<>();
        try {
            Statement st = conn().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products ORDER BY product_id");
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ProductDAO] getAllProducts: " + e.getMessage());
        }
        return list;
    }

    public List<ProductBean> searchProducts(String keyword) {
        List<ProductBean> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE product_name LIKE ? OR category LIKE ?";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ProductDAO] searchProducts: " + e.getMessage());
        }
        return list;
    }

    public List<ProductBean> getLowStockProducts() {
        List<ProductBean> list = new ArrayList<>();
        try {
            Statement st = conn().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products WHERE quantity <= reorder_level");
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ProductDAO] getLowStockProducts: " + e.getMessage());
        }
        return list;
    }

    public ProductBean getProductById(int id) {
        try {
            PreparedStatement ps = conn().prepareStatement("SELECT * FROM products WHERE product_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] getProductById: " + e.getMessage());
        }
        return null;
    }

    public boolean updateProduct(ProductBean p) {
        String sql = "UPDATE products SET product_name=?,category=?,quantity=?,price=?,reorder_level=? WHERE product_id=?";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getCategory());
            ps.setInt   (3, p.getQuantity());
            ps.setDouble(4, p.getPrice());
            ps.setInt   (5, p.getReorderLevel());
            ps.setInt   (6, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] updateProduct: " + e.getMessage());
            return false;
        }
    }
public boolean updateQuantity(int productId, int newQty) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "UPDATE products SET quantity=? WHERE product_id=?");
            ps.setInt(1, newQty);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] updateQuantity: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM products WHERE product_id=?");
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] deleteProduct: " + e.getMessage());
            return false;
        }
    }

    public int getTotalProductCount() {
        try {
            ResultSet rs = conn().createStatement()
                .executeQuery("SELECT COUNT(*) FROM products");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] count: " + e.getMessage());
        }
        return 0;
    }

    private ProductBean mapRow(ResultSet rs) throws SQLException {
        return new ProductBean(
            rs.getInt("product_id"),
            rs.getString("product_name"),
            rs.getString("category"),
            rs.getInt("quantity"),
            rs.getDouble("price"),
            rs.getInt("reorder_level")
        );
    }
}

