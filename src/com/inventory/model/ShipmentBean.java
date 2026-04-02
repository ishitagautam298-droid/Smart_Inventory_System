package com.inventory.model;

import java.io.Serializable;

// OOPs: Encapsulation | JavaBean
public class ShipmentBean implements Serializable {
    private static final long serialVersionUID = 5L;

    private int    shipmentId;
    private String trackingNumber;
    private int    orderId;
    private String productName;
    private String fromLocation;
    private String toLocation;
    private String carrier;
    private String estimatedArrival;
    private String status; // PENDING | IN TRANSIT | SHIPPED | DELIVERED

    public ShipmentBean() {}

    public ShipmentBean(int shipmentId, String trackingNumber, int orderId,
                        String productName, String fromLocation, String toLocation,
                        String carrier, String estimatedArrival, String status) {
        this.shipmentId       = shipmentId;
        this.trackingNumber   = trackingNumber;
        this.orderId          = orderId;
        this.productName      = productName;
        this.fromLocation     = fromLocation;
        this.toLocation       = toLocation;
        this.carrier          = carrier;
        this.estimatedArrival = estimatedArrival;
        this.status           = status;
    }

    public boolean isDelivered() { return "DELIVERED".equals(status); }

    // Getters & Setters
    public int    getShipmentId()               { return shipmentId; }
    public void   setShipmentId(int v)          { this.shipmentId = v; }
    public String getTrackingNumber()           { return trackingNumber; }
    public void   setTrackingNumber(String v)   { this.trackingNumber = v; }
    public int    getOrderId()                  { return orderId; }
    public void   setOrderId(int v)             { this.orderId = v; }
    public String getProductName()              { return productName; }
    public void   setProductName(String v)      { this.productName = v; }
    public String getFromLocation()             { return fromLocation; }
    public void   setFromLocation(String v)     { this.fromLocation = v; }
    public String getToLocation()               { return toLocation; }
    public void   setToLocation(String v)       { this.toLocation = v; }
    public String getCarrier()                  { return carrier; }
    public void   setCarrier(String v)          { this.carrier = v; }
    public String getEstimatedArrival()         { return estimatedArrival; }
    public void   setEstimatedArrival(String v) { this.estimatedArrival = v; }
    public String getStatus()                   { return status; }
    public void   setStatus(String v)           { this.status = v; }

    @Override
    public String toString() { return trackingNumber + " - " + productName; }
}
