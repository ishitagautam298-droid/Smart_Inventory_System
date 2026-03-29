package com.inventory.backend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.inventory.model.Order;
import com.inventory.model.Product;
import com.inventory.model.Supplier;
import com.inventory.model.User;

public final class InventoryDataStore {
    private static final InventoryDataStore INSTANCE = new InventoryDataStore();

    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private final Map<Integer, Supplier> suppliers = new LinkedHashMap<>();
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private final Map<String, User> users = new LinkedHashMap<>();

    private final AtomicInteger productIdSequence = new AtomicInteger(1);
    private final AtomicInteger supplierIdSequence = new AtomicInteger(1);
    private final AtomicInteger orderIdSequence = new AtomicInteger(1);

    private InventoryDataStore() {
        seedUsers();
        seedProducts();
        seedSuppliers();
    }

    public static InventoryDataStore getInstance() {
        return INSTANCE;
    }

    private void seedUsers() {
        users.put("admin", new User("admin", "admin", "ADMIN"));
        users.put("manager", new User("manager", "manager123", "MANAGER"));
    }

    private void seedProducts() {
        saveProduct(new Product(0, "Laptop", 12, 65000.0));
        saveProduct(new Product(0, "Mouse", 40, 899.0));
        saveProduct(new Product(0, "Keyboard", 25, 1499.0));
    }

    private void seedSuppliers() {
        saveSupplier(new Supplier(0, "Tech World", "+91-9999999999", "Pune"));
        saveSupplier(new Supplier(0, "Office Hub", "+91-8888888888", "Mumbai"));
    }

    public synchronized User findUser(String username) {
        return users.get(username);
    }

    public synchronized Product saveProduct(Product product) {
        if (product.getId() <= 0) {
            product.setId(productIdSequence.getAndIncrement());
        }
        products.put(product.getId(), product);
        return product;
    }

    public synchronized Supplier saveSupplier(Supplier supplier) {
        if (supplier.getId() <= 0) {
            supplier.setId(supplierIdSequence.getAndIncrement());
        }
        suppliers.put(supplier.getId(), supplier);
        return supplier;
    }

    public synchronized Order saveOrder(Order order) {
        if (order.getId() <= 0) {
            order.setId(orderIdSequence.getAndIncrement());
        }
        orders.put(order.getId(), order);
        return order;
    }

    public synchronized Product findProductById(int productId) {
        return products.get(productId);
    }

    public synchronized List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public synchronized List<Supplier> getSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public synchronized List<Order> getOrders() {
        return new ArrayList<>(orders.values());
    }
}
