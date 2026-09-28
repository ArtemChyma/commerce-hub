package com.commercehub.cart;

import com.commercehub.product.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

public class Cart {

    private final Map<UUID, CartItem> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity);
        items.merge(
                product.getId(),
                new CartItem(product, quantity),
                (oldItem, newItem) ->
                        new CartItem(
                                oldItem.product(),
                                oldItem.quantity() + newItem.quantity()
                        )
        );
    }

    public void updateQuantity(UUID productId, int quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        items.compute(productId, (id, currentItem) -> {
            if (currentItem == null) {
                throw new IllegalArgumentException(
                        "Product not found in cart: " + productId
                );
            }

            return new CartItem(currentItem.product(), quantity);
        });
    }

    public void removeProduct(UUID productId) {
        validateProductId(productId);
        items.remove(productId);
    }

    public Optional<CartItem> findItem(UUID productId) {
        validateProductId(productId);
        return Optional.ofNullable(items.get(productId));
    }

    public BigDecimal getTotal() {
        return items.values()
                .stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<UUID, CartItem> getItems() {
        return Map.copyOf(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private static void validateProductId(UUID productId) {
        if (productId == null)
            throw new IllegalArgumentException("product id must be not null");
    }
    private static void validateProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("product must not be null");
    }
    private static void validateQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity of a product must be not less or equal zero");
    }
}
