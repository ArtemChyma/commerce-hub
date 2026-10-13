package com.commercehub.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID orderId;
    private final BigDecimal amount;
    private PaymentStatus status;
    private final Instant createdAt;

    public Payment(UUID orderId, BigDecimal amount) {
        validateOrderId(orderId);
        validateAmount(amount);

        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.amount = amount;
        this.createdAt = Instant.now();
        this.status = PaymentStatus.PENDING;
    }

    public void markSucceeded() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Status must be PENDING");
        }

        status = PaymentStatus.SUCCEEDED;
    }

    public void markFailed() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Status must be PENDING");
        }

        status = PaymentStatus.FAILED;
    }

    private static void validateOrderId(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException(
                    "Order id must not be null"
            );
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than 0"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
