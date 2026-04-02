package com.inventory.service;

import com.inventory.backend.ProductDAO;
import com.inventory.model.ProductBean;
import java.util.List;

/**
 * OOPs: Abstraction — business logic sits here, not in the UI.
 * Service layer talks to DAO and returns results to Controller.
 */
public class InventoryService {

    // OOPs: Composition — InventoryService HAS-A ProductDAO
    private final ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(ProductBean p) {
        if (p.getProductName() == null
                || p.getProductName().trim().isEmpty()) return false;
        if (p.getPrice() < 0 || p.getQuantity() < 0)  return false;
        return productDAO.addProduct(p);
    }

    public List<ProductBean> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<ProductBean> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty())
            return getAllProducts();
        return productDAO.searchProducts(keyword.trim());
    }

    public List<ProductBean> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }

    public int getLowStockCount() {
        return productDAO.getLowStockProducts().size();
    }

    public boolean updateProduct(ProductBean p) {
        return productDAO.updateProduct(p);
    }

    public boolean adjustStock(int productId, int newQuantity) {
        if (newQuantity < 0) return false;
        return productDAO.updateQuantity(productId, newQuantity);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }

    public int getTotalProductCount() {
        return productDAO.getTotalProductCount();
    }

    public ProductBean getProductById(int id) {
        return productDAO.getProductById(id);
    }
}
