package com.inventory.model;

import java.io.Serializable;

// OOPs: Encapsulation | JavaBean Contract: Serializable + no-arg constructor
public class ProductBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int    productId;
    private String productName;
    private String category;
    private int    quantity;
    private double price;
    private int    reorderLevel;

    public ProductBean() {}

    public ProductBean(int productId, String productName, String category,
                       int quantity, double price, int reorderLevel) {
        this.productId    = productId;
        this.productName  = productName;
        this.category     = category;
        this.quantity     = quantity;
        this.price        = price;
        this.reorderLevel = reorderLevel;
    }

    // Business logic method
    public String getStockStatus() {
        if (quantity <= 0)            return "OUT OF STOCK";
        if (quantity <= reorderLevel) return "LOW STOCK";
        return "IN STOCK";
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    // Getters & Setters
    public int    getProductId()            { return productId; }
    public void   setProductId(int v)       { this.productId = v; }
    public String getProductName()          { return productName; }
    public void   setProductName(String v)  { this.productName = v; }
    public String getCategory()             { return category; }
    public void   setCategory(String v)     { this.category = v; }
    public int    getQuantity()             { return quantity; }
    public void   setQuantity(int v)        { this.quantity = v; }
    public double getPrice()                { return price; }
    public void   setPrice(double v)        { this.price = v; }
    public int    getReorderLevel()         { return reorderLevel; }
    public void   setReorderLevel(int v)    { this.reorderLevel = v; }

    @Override
    public String toString() { return productName; }
}

