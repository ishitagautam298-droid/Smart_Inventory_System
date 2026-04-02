package com.inventory.model;

import java.io.Serializable;

// OOPs: Encapsulation | JavaBean
public class SupplierBean implements Serializable {
    private static final long serialVersionUID = 3L;

    private int    supplierId;
    private String supplierName;
    private String contactPerson;
    private String email;
    private String phone;
    private String productCategory;
    private int    leadTimeDays;
    private String status; // ACTIVE | INACTIVE

    public SupplierBean() {}

    public SupplierBean(int supplierId, String supplierName, String contactPerson,
                        String email, String phone, String productCategory,
                        int leadTimeDays, String status) {
        this.supplierId      = supplierId;
        this.supplierName    = supplierName;
        this.contactPerson   = contactPerson;
        this.email           = email;
        this.phone           = phone;
        this.productCategory = productCategory;
        this.leadTimeDays    = leadTimeDays;
        this.status          = status;
    }

    public boolean isActive() { return "ACTIVE".equals(status); }

    // Getters & Setters
    public int    getSupplierId()               { return supplierId; }
    public void   setSupplierId(int v)          { this.supplierId = v; }
    public String getSupplierName()             { return supplierName; }
    public void   setSupplierName(String v)     { this.supplierName = v; }
    public String getContactPerson()            { return contactPerson; }
    public void   setContactPerson(String v)    { this.contactPerson = v; }
    public String getEmail()                    { return email; }
    public void   setEmail(String v)            { this.email = v; }
    public String getPhone()                    { return phone; }
    public void   setPhone(String v)            { this.phone = v; }
    public String getProductCategory()          { return productCategory; }
    public void   setProductCategory(String v)  { this.productCategory = v; }
    public int    getLeadTimeDays()             { return leadTimeDays; }
    public void   setLeadTimeDays(int v)        { this.leadTimeDays = v; }
    public String getStatus()                   { return status; }
    public void   setStatus(String v)           { this.status = v; }

    @Override
    public String toString() { return supplierName; }
}
