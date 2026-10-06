package com.commercehub.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
        UUID productId,
        String productName,
        BigDecimal unitPrice,
        int quantity
) {

    public OrderItem{
        validateProductId(productId);
        validateProductName(productName);
        validateProductPrice(unitPrice);
        validateProductQuantity(quantity);
    }

    private static void validateProductId(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id must not be null");
        }
    }

    private static void validateProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or blank");
        }
    }

    private static void validateProductPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must not be less than 0");
        }
    }

    private static void validateProductQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal quantity = new BigDecimal(quantity());
        return unitPrice.multiply(quantity);
    }
}
