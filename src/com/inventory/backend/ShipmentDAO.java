package com.inventory.backend;

import com.inventory.model.ShipmentBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC: Full CRUD for shipments table.
 * Runtime fix: null-check connection before every operation.
 */
public class ShipmentDAO {

    private Connection conn() throws SQLException {
        Connection c = DBConnection.getConnection();
        if (c == null) throw new SQLException("No database connection available.");
        return c;
    }

    public boolean addShipment(ShipmentBean s) {
        String sql = "INSERT INTO shipments(tracking_number,order_id,product_name,from_location,to_location,carrier,estimated_arrival,status) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn().prepareStatement(sql);
            ps.setString(1, s.getTrackingNumber());
            ps.setInt   (2, s.getOrderId());
            ps.setString(3, s.getProductName());
            ps.setString(4, s.getFromLocation());
            ps.setString(5, s.getToLocation());
            ps.setString(6, s.getCarrier());
            ps.setString(7, s.getEstimatedArrival());
            ps.setString(8, s.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ShipmentDAO] addShipment: " + e.getMessage());
            return false;
        }
    }

    public List<ShipmentBean> getAllShipments() {
        List<ShipmentBean> list = new ArrayList<>();
        try {
            ResultSet rs = conn().createStatement()
                .executeQuery("SELECT * FROM shipments ORDER BY shipment_id DESC");
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ShipmentDAO] getAllShipments: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatus(int shipmentId, String status) {
        try {
            PreparedStatement ps = conn().prepareStatement(
                "UPDATE shipments SET status=? WHERE shipment_id=?");
            ps.setString(1, status);
            ps.setInt   (2, shipmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ShipmentDAO] updateStatus: " + e.getMessage());
            return false;
        }
    }

    private ShipmentBean mapRow(ResultSet rs) throws SQLException {
        return new ShipmentBean(
            rs.getInt   ("shipment_id"),
            rs.getString("tracking_number"),
            rs.getInt   ("order_id"),
            rs.getString("product_name"),
            rs.getString("from_location"),
            rs.getString("to_location"),
            rs.getString("carrier"),
            rs.getString("estimated_arrival"),
            rs.getString("status")
        );
    }
}
