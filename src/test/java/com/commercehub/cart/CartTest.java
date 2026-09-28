package com.commercehub.cart;

import com.commercehub.product.Category;
import com.commercehub.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.instrument.UnmodifiableClassException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    public void shouldAddProductsProperly() {
        Cart cart = new Cart();

        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );

        cart.addProduct(product, 2);
        cart.addProduct(product, 3);

        Optional<CartItem> item = cart.findItem(product.getId());

        assertTrue(item.isPresent());
        assertEquals(5, item.orElseThrow().quantity());
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void shouldUpdateProductQuantity() {
        Cart cart = new Cart();

        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );

        cart.addProduct(product, 2);

        cart.updateQuantity(product.getId(), 7);

        Optional<CartItem> item = cart.findItem(product.getId());
        assertTrue(item.isPresent());
        assertEquals(7, item.get().quantity());
    }

    @Test
    public void shouldThrowWhenUpdatingMissingProduct() {
        Cart cart = new Cart();

        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );

        cart.addProduct(product, 10);

        UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () ->
                cart.updateQuantity(id, 5));
        assertEquals(
                10,
                cart.findItem(product.getId()).orElseThrow().quantity()
        );
    }

    @Test
    public void shouldRemoveProduct() {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        cart.addProduct(product, 10);

        cart.removeProduct(product.getId());

        assertEquals(Optional.empty(), cart.findItem(product.getId()));
        assertTrue(cart.isEmpty());
    }

    @Test
    public void shouldCalculateTotal() {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        Product product2 = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("5.50"),
                Category.ELECTRONICS
        );

        cart.addProduct(product, 2);
        cart.addProduct(product2, 3);

        assertEquals(new BigDecimal("42.48"), cart.getTotal());
    }

    @Test
    public void shouldReturnZeroForEmptyCart() {
        Cart cart = new Cart();
        assertTrue(cart.isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotal());
    }

    @Test
    public void shouldReturnUnmodifiableItems() {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        cart.addProduct(product, 2);

        Map<UUID, CartItem> items = cart.getItems();

        assertThrows(UnsupportedOperationException.class, () ->
                items.remove(product.getId()));
        assertTrue(cart.findItem(product.getId()).isPresent());
    }

    @Test
    public void shouldReturnSnapshotOfItems() {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        Product product2 = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("5.50"),
                Category.ELECTRONICS
        );
        cart.addProduct(product, 2);

        Map<UUID, CartItem> items = cart.getItems();

        cart.addProduct(product2, 3);
        assertEquals(1, items.size());
        assertEquals(2, cart.getItems().size());
        assertTrue(items.containsKey(product.getId()));
        assertFalse(items.containsKey(product2.getId()));
        assertTrue(cart.getItems().containsKey(product2.getId()));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100, 0})
    public void shouldRejectNonPositiveQuantity(int nonPositiveQuantity) {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        assertThrows(IllegalArgumentException.class, () ->
                cart.
                        addProduct(product, nonPositiveQuantity)
        );
        assertTrue(cart.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100, 0})
    public void shouldRejectNonPositiveQuantityOnUpdate(int nonPositiveQuantity) {
        Cart cart = new Cart();
        Product product = new Product(
                "product",
                "this product is used to make people laugh",
                new BigDecimal("12.99"),
                Category.ELECTRONICS
        );
        cart.addProduct(product, 5);

        assertThrows(IllegalArgumentException.class,
                () ->
                cart.
                        updateQuantity(product.getId(), nonPositiveQuantity)
        );

        assertEquals(
                5,
                cart.findItem(product.getId()).orElseThrow().quantity()
        );
    }

    @Test
    public void shouldRejectNullProduct() {
        Cart cart = new Cart();

        assertThrows(IllegalArgumentException.class,
                () ->
                        cart.addProduct(null, 2)
        );
        assertTrue(cart.isEmpty());
    }

    @Test
    public void shouldRejectNullProductId() {
        Cart cart = new Cart();

        assertThrows(IllegalArgumentException.class,
                () ->
                        cart.updateQuantity(null, 2)
        );
        assertThrows(IllegalArgumentException.class,
                () ->
                        cart.removeProduct(null)
        );
        assertThrows(IllegalArgumentException.class,
                () ->
                        cart.findItem(null)
        );
    }
}
