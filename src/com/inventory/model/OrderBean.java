package com.inventory.model;

import java.io.Serializable;

// OOPs: Encapsulation | JavaBean
public class OrderBean implements Serializable {
    private static final long serialVersionUID = 2L;

    private int    orderId;
    private int    productId;
    private String productName;
    private int    supplierId;
    private String supplierName;
    private int    quantity;
    private double totalAmount;
    private String orderDate;
    private String status; // PENDING | PROCESSING | SHIPPED | DELIVERED | CANCELLED

    public OrderBean() {}

    public OrderBean(int orderId, int productId, String productName,
                     int supplierId, String supplierName,
                     int quantity, double totalAmount,
                     String orderDate, String status) {
        this.orderId      = orderId;
        this.productId    = productId;
        this.productName  = productName;
        this.supplierId   = supplierId;
        this.supplierName = supplierName;
        this.quantity     = quantity;
        this.totalAmount  = totalAmount;
        this.orderDate    = orderDate;
        this.status       = status;
    }

    public boolean isCancellable() {
        return status.equals("PENDING") || status.equals("PROCESSING");
    }

    // Getters & Setters
    public int    getOrderId()              { return orderId; }
    public void   setOrderId(int v)         { this.orderId = v; }
    public int    getProductId()            { return productId; }
    public void   setProductId(int v)       { this.productId = v; }
    public String getProductName()          { return productName; }
    public void   setProductName(String v)  { this.productName = v; }
    public int    getSupplierId()           { return supplierId; }
    public void   setSupplierId(int v)      { this.supplierId = v; }
    public String getSupplierName()         { return supplierName; }
    public void   setSupplierName(String v) { this.supplierName = v; }
    public int    getQuantity()             { return quantity; }
    public void   setQuantity(int v)        { this.quantity = v; }
    public double getTotalAmount()          { return totalAmount; }
    public void   setTotalAmount(double v)  { this.totalAmount = v; }
    public String getOrderDate()            { return orderDate; }
    public void   setOrderDate(String v)    { this.orderDate = v; }
    public String getStatus()               { return status; }
    public void   setStatus(String v)       { this.status = v; }

    @Override
    public String toString() { return "Order#" + orderId + " - " + productName; }
}
