package com.commercehub.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final UUID userId;
    private final List<OrderItem> items;
    private final BigDecimal total;
    private OrderStatus status;
    private final Instant createdAt;

    public Order(UUID userId, List<OrderItem> items) {
        validateUserId(userId);
        validateOrderItems(items);
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.items = List.copyOf(items);
        this.total = calculateTotal();
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    private static void validateUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
    }
    private static void validateOrderItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Item list must not be null nor empty");
        }
    }
    private BigDecimal calculateTotal() {
        return items.stream().reduce(BigDecimal.ZERO, (price,item) -> price.add(item.getSubtotal()), BigDecimal::add);
    }

    public void startPayment() {
        if (status != (OrderStatus.CREATED)) {
            throw new IllegalStateException("Order status must be in a CREATED state");
        }
        status = OrderStatus.PENDING_PAYMENT;
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order status must be in a PENDING_PAYMENT state");
        }
        status = OrderStatus.PAID;
    }
    public void markPaymentFailed() {
        if (status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order status must be in a PENDING_PAYMENT state");
        }
        status = OrderStatus.PAYMENT_FAILED;
    }
    public void cancel() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order status must be in a CREATED state");
        }
        status = OrderStatus.CANCELLED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
