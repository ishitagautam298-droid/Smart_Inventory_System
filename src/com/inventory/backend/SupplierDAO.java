package com.inventory.backend;

import com.inventory.model.SupplierBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC: Full CRUD for suppliers table.
 * Runtime fix: null-check connection before every operation.
 */
public class SupplierDAO {

    private Connection conn() throws SQLException {
        Connection c = DBConnection.getConnection();
        if (c == null) throw new SQLException("No database connection available.");
        return c;
    }

    public boolean addSupplier(SupplierBean s) {
        String sql = "INSERT INTO suppliers(supplier_name,contact_person,email,phone,product_category,lead_time_days,status) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getProductCategory());
            ps.setInt   (6, s.getLeadTimeDays());
            ps.setString(7, s.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] addSupplier: " + e.getMessage());
            return false;
        }
    }

    public List<SupplierBean> getAllSuppliers() {
        List<SupplierBean> list = new ArrayList<>();
        try {
            ResultSet rs = conn().createStatement()
                .executeQuery("SELECT * FROM suppliers ORDER BY supplier_id");
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] getAllSuppliers: " + e.getMessage());
        }
        return list;
    }

    public List<SupplierBean> getActiveSuppliers() {
        List<SupplierBean> list = new ArrayList<>();
        try {
            ResultSet rs = conn().createStatement()
                .executeQuery("SELECT * FROM suppliers WHERE status='ACTIVE'");
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] getActiveSuppliers: " + e.getMessage());
        }
        return list;
    }

    public boolean updateSupplier(SupplierBean s) {
        String sql = "UPDATE suppliers SET supplier_name=?,contact_person=?,email=?,phone=?,product_category=?,lead_time_days=?,status=? WHERE supplier_id=?";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getProductCategory());
            ps.setInt   (6, s.getLeadTimeDays());
            ps.setString(7, s.getStatus());
            ps.setInt   (8, s.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] updateSupplier: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSupplier(int id) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "DELETE FROM suppliers WHERE supplier_id=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] deleteSupplier: " + e.getMessage());
            return false;
        }
    }

    public SupplierBean getSupplierById(int id) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "SELECT * FROM suppliers WHERE supplier_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] getById: " + e.getMessage());
        }
        return null;
    }
private SupplierBean mapRow(ResultSet rs) throws SQLException {
        return new SupplierBean(
            rs.getInt   ("supplier_id"),
            rs.getString("supplier_name"),
            rs.getString("contact_person"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("product_category"),
            rs.getInt   ("lead_time_days"),
            rs.getString("status")
        );
    }
}
