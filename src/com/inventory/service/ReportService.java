package com.inventory.service;

import java.util.List;

import com.inventory.model.Order;
import com.inventory.model.Product;
import com.inventory.model.Supplier;

public class ReportService {
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final OrderService orderService = new OrderService();

    public String generateInventoryReport() {
        List<Product> products = productService.getAllProducts();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Order> orders = orderService.getAllOrders();

        int totalUnits = 0;
        double inventoryValue = 0;
        StringBuilder lowStock = new StringBuilder();

        for (Product product : products) {
            totalUnits += product.getQuantity();
            inventoryValue += product.getQuantity() * product.getPrice();
            if (product.getQuantity() <= 5) {
                lowStock.append("- ")
                    .append(product.getName())
                    .append(" (ID: ")
                    .append(product.getId())
                    .append(", Qty: ")
                    .append(product.getQuantity())
                    .append(")\n");
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("SMART INVENTORY SYSTEM REPORT\n");
        report.append("=============================\n\n");
        report.append("Overview\n");
        report.append("Total Products: ").append(products.size()).append('\n');
        report.append("Total Suppliers: ").append(suppliers.size()).append('\n');
        report.append("Total Orders: ").append(orders.size()).append('\n');
        report.append("Units In Stock: ").append(totalUnits).append('\n');
        report.append(String.format("Estimated Inventory Value: %.2f%n%n", inventoryValue));

        report.append("Products\n");
        for (Product product : products) {
            report.append("- ID: ").append(product.getId())
                .append(", Name: ").append(product.getName())
                .append(", Qty: ").append(product.getQuantity())
                .append(", Price: ").append(product.getPrice())
                .append('\n');
        }

        report.append("\nSuppliers\n");
        for (Supplier supplier : suppliers) {
            report.append("- ID: ").append(supplier.getId())
                .append(", Name: ").append(supplier.getName())
                .append(", Contact: ").append(supplier.getContact())
                .append('\n');
        }

        report.append("\nOrders\n");
        if (orders.isEmpty()) {
            report.append("- No orders placed yet.\n");
        } else {
            for (Order order : orders) {
                report.append("- ID: ").append(order.getId())
                    .append(", Product ID: ").append(order.getProductId())
                    .append(", Qty: ").append(order.getQuantity())
                    .append(", Date: ").append(order.getOrderDate())
                    .append('\n');
            }
        }

        report.append("\nLow Stock Alerts\n");
        report.append(lowStock.length() == 0 ? "- No low stock items.\n" : lowStock.toString());
        return report.toString();
    }
}
