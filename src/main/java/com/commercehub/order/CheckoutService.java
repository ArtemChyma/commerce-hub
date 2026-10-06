package com.commercehub.order;

import com.commercehub.cart.Cart;
import com.commercehub.cart.CartItem;
import com.commercehub.inventory.Inventory;
import com.commercehub.user.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CheckoutService {
    private final Inventory inventory;

    public CheckoutService(Inventory inventory) {
        validateInventory(inventory);
        this.inventory = inventory;
    }

    public Order checkout(User user, Cart cart) {
        validateUser(user);
        validateCart(cart);
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        Map<UUID, CartItem> cartItems = cart.getItems();
        List<OrderItem> orderItems = cartItems.
                values().
                stream().
                map(
                        item
                                ->
                                new OrderItem(item.product().getId(),
                                        item.product().getName(),
                                        item.product().getPrice(),
                                        item.quantity())
                ).toList();
        Map<UUID, Integer> itemsToRemove = orderItems.
                stream().
                collect(
                        Collectors.toMap(
                                OrderItem::productId, OrderItem::quantity
                        )
                );
        Order order = new Order(user.id(), orderItems);

        inventory.removeStock(itemsToRemove);

        return order;
    }
    private static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User instance must be not null");
        }
    }
    private static void validateCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart instance must be not null");
        }
    }
    private static void validateInventory(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory instance must be not null");
        }
    }

}
