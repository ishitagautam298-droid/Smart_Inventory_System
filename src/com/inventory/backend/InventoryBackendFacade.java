package com.inventory.backend;

import java.util.List;

import com.inventory.model.Order;
import com.inventory.model.Product;
import com.inventory.model.Supplier;
import com.inventory.model.User;
import com.inventory.service.AuthService;
import com.inventory.service.OrderService;
import com.inventory.service.ProductService;
import com.inventory.service.ReportService;
import com.inventory.service.SupplierService;

/**
 * Central backend entry point for the Smart Inventory System.
 * This file is intended to keep the backend contribution visible and easy to
 * review in one place while delegating detailed logic to focused services.
 */
public final class InventoryBackendFacade {
    private static final InventoryBackendFacade INSTANCE = new InventoryBackendFacade();

    private final AuthService authService = new AuthService();
    private final ProductService productService = new ProductService();
    private final SupplierService supplierService = new SupplierService();
    private final OrderService orderService = new OrderService();
    private final ReportService reportService = new ReportService();

    private InventoryBackendFacade() {
    }

    public static InventoryBackendFacade getInstance() {
        return INSTANCE;
    }

    public User login(String username, String password) {
        return authService.login(username, password);
    }

    public Product addProduct(Product product) {
        return productService.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public Supplier addSupplier(Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    public Order placeOrder(Order order) {
        return orderService.placeOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public String generateInventoryReport() {
        return reportService.generateInventoryReport();
    }
}
