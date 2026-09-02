package com.commercehub.product;

import java.util.*;

public class ProductCatalog {
    private final Map<UUID, Product> products;

    public ProductCatalog() {
        this.products = new HashMap<>();
    }

    public ProductCatalog(Map<UUID, Product> products) {
        this.products = defensiveCopy(products);
    }

    public void addProduct(Product product) {
        checkProduct(product);
        if (!products.containsKey(product.getId()))
            this.products.put(product.getId(), product);
        else
            throw new IllegalArgumentException("Product already exists");
    }

    public void updateProduct(Product product) {
        checkProduct(product);

        if (products.containsKey(product.getId()))
            this.products.put(product.getId(), product);
        else
            throw new IllegalArgumentException("Product does not exist");
    }

    public void removeProduct(UUID id) {
        checkId(id);
        products.remove(id);
    }

    public Optional<Product> findById(UUID id) {
        checkId(id);
        return Optional.ofNullable(products.get(id));
    }

    public Map<UUID, Product> getAllProducts() {
        return defensiveCopy(products);
    }

    private Map<UUID, Product> defensiveCopy(Map<UUID, Product> products) {
        Map<UUID, Product> copy = new HashMap<>();
        Set<Map.Entry<UUID, Product>> entries = products.entrySet();

        for (Map.Entry<UUID, Product> entry : entries) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    private void checkProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    private void checkId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Product Id cannot be null");
        }
    }
}
