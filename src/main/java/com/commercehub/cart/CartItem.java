package com.commercehub.cart;

import com.commercehub.product.Product;

import java.math.BigDecimal;

public record CartItem(
        Product product,
        int quantity
) {

    public CartItem {
        if (product == null)
            throw new IllegalArgumentException("product must not be null");
        if (quantity <= 0)
            throw new IllegalArgumentException("cart item quantity must not be less or equal zero");
    }
    public BigDecimal getSubtotal() {
        BigDecimal quantity = new BigDecimal(quantity());
        return product.getPrice().multiply(quantity);
    }
}
