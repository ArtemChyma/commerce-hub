package com.commercehub.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    private final Map<UUID, Integer> stock;

    public Inventory() {
        this.stock = new ConcurrentHashMap<>();
    }

    public Inventory(Map<UUID, Integer> stock) {
        if (stock == null) {
            throw new IllegalArgumentException("stock to be copied must be not null");
        }
        Set<Map.Entry<UUID, Integer>> stockSet = stock.entrySet();
        for (Map.Entry<UUID, Integer> entry : stockSet) {
            validateProductId(entry.getKey());
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("quantity for a product must be not null");
            }
            validateStockQuantity(entry.getValue());
        }
        this.stock = new ConcurrentHashMap<>(stock);
    }

    public void addStock(UUID productId, int quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        stock.merge(productId, quantity, Integer::sum);
    }

    public int getStock(UUID productId) {
        validateProductId(productId);
        return stock.getOrDefault(productId, 0);
    }

    public boolean isAvailable(UUID productId, int quantity) {
        validateQuantity(quantity);

        return getStock(productId) >= quantity;
    }

    public void removeStock(UUID productId, int quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        stock.compute(productId, (id, currentState) -> {
            if (currentState == null) {
                throw new IllegalArgumentException("product does not exist in inventory");
            }
            if (currentState >= quantity) {
                return currentState - quantity;
            }
            else {
                throw new IllegalArgumentException("quantity of a specific product is not available in the stock");
            }
        });
    }


    private static void validateProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("product id must not be null");
        }
    }
    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
    }
    private static void validateStockQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("stock quantity must be greater or equal zero");
        }
    }
}