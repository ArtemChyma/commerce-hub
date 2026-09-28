package com.commercehub.cart;

import com.commercehub.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class Cart {

    private final Map<UUID, CartItem> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity);
        items.merge(product.getId(), items.get(product.getId()), new BiFunction<CartItem, CartItem, CartItem>() {
            @Override
            public CartItem apply(CartItem cartItem, CartItem cartItem2) {
                return new CartItem(cartItem(cartItem.product().getId(), );
            }
        });
        if (items.computeIfPresent(product.getId(),
                (id, calculatedItem) ->
                        new CartItem(product,
                                calculatedItem.quantity() + quantity)) == null)
            items.put(product.getId(), new CartItem(product, quantity));
    }

    public void updateQuantity(UUID productId, int quantity) {
        validateProductId(productId);
        validateQuantity(quantity);

        items.computeIfPresent(productId,
                (id, updatedItem)
                        -> new CartItem(items.get(productId).product(), quantity));
    }

    private static void validateProductId(UUID productId) {
        if (productId == null)
            throw new IllegalArgumentException("product id must be not null")
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
