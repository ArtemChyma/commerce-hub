package com.commercehub.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void shouldCreateValidProduct() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.99");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(category, product.getCategory());
        assertNotNull(product.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-100", "-12.55", "-0.01", "0", "0.00"})
    void shouldRejectNonPositivePriceOnCreation(String nonPositivePrice) {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal(nonPositivePrice);
        Category category = Category.ELECTRONICS;

        assertThrows(IllegalArgumentException.class,
                () -> new Product(name, description, price, category));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-100", "-12.55", "-0.01", "0", "0.00"})
    void shouldRejectNonPositivePriceOnUpdate(String nonPositivePrice) {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal positivePrice = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, positivePrice, category);
        BigDecimal negativePrice = new BigDecimal(nonPositivePrice);

        assertThrows(IllegalArgumentException.class,
                () -> product.setPrice(negativePrice));
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {" ", "   "})
    void shouldRejectInvalidName(String invalidName) {
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        assertThrows(IllegalArgumentException.class,
                () -> new Product(invalidName, description, price, category));
    }

    @Test
    void shouldNormalizeNullDescription() {
        String name = "product";
        String description = null;
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertEquals("", product.getDescription());
    }

    @Test
    void shouldRejectNullPriceOnCreation() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = null;
        Category category = Category.ELECTRONICS;

        assertThrows(IllegalArgumentException.class, () -> new Product(name, description, price, category));
    }

    @Test
    void shouldRejectNullPriceOnUpdate() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertThrows(IllegalArgumentException.class, () -> product.setPrice(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   "})
    @NullSource
    @EmptySource
    void shouldRejectInvalidNameOnUpdate(String invalidName) {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertThrows(IllegalArgumentException.class, () -> product.setName(invalidName));
    }

    @Test
    void shouldRejectNullCategoryOnCreation() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = null;

        assertThrows(IllegalArgumentException.class, () -> new Product(name, description, price, category));
    }

    @Test
    void shouldRejectNullCategoryOnUpdate() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertThrows(IllegalArgumentException.class, () -> product.setCategory(null));
    }

    @Test
    void shouldBeEqualToItself() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertEquals(product, product);
    }

    @Test
    void shouldNotBeEqualToNull() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);

        assertNotEquals(null, product);
    }

    @Test
    void shouldNotBeEqualWhenProductsHaveDifferentIds() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product1 = new Product(name, description, price, category);
        Product product2 = new Product(name, description, price, category);

        assertNotEquals(product2, product1);
    }

    @Test
    void shouldKeepSameHashCodeWhenMutableFieldsChange() {
        String name = "product";
        String description = "this product is used to make people laugh";
        BigDecimal price = new BigDecimal("12.55");
        Category category = Category.ELECTRONICS;

        Product product = new Product(name, description, price, category);
        int hashCodeBeforeChange = product.hashCode();

        product.setName("product1");
        product.setDescription("this product is used to make you happy");
        product.setPrice(new BigDecimal("24.99"));
        product.setCategory(Category.HOME);

        int hashCodeAfterChange = product.hashCode();

        assertEquals(hashCodeBeforeChange, hashCodeAfterChange);
    }
}
