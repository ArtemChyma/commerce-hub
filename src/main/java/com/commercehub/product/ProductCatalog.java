package com.commercehub.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductCatalog {
    private final Map<UUID, Product> products;

    public ProductCatalog() {
        this.products = new HashMap<>();
    }

    public ProductCatalog(Map<UUID, Product> products) {
        this.products = defensiveCopy(products);
    }

    public void addProduct(Product product) {
        checkProduct(product);
        if (!products.containsKey(product.getId()))
            this.products.put(product.getId(), product);
        else
            throw new IllegalArgumentException("Product already exists");
    }

    public void updateProduct(Product product) {
        checkProduct(product);

        if (products.containsKey(product.getId()))
            this.products.put(product.getId(), product);
        else
            throw new IllegalArgumentException("Product does not exist");
    }

    public void removeProduct(UUID id) {
        checkId(id);
        products.remove(id);
    }

    public Optional<Product> findById(UUID id) {
        checkId(id);
        return Optional.ofNullable(products.get(id));
    }

    //returns unmodifiable list because of .toList()
    public List<Product> findByCategory(Category category) {
        if (category == null){
            throw new IllegalArgumentException("Category must not be null");
        }

        return products.values().stream().filter(product -> product.getCategory() == category).toList();
    }

    public List<Product> findByName(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        String lowerCaseName = name.toLowerCase();
        return products.values().stream().filter(product -> product.getName().toLowerCase().contains(lowerCaseName)).toList();
    }

    public List<Product> findByPriceRange(BigDecimal minPrice,
                                          BigDecimal maxPrice) {
        if (maxPrice == null || minPrice == null || minPrice.compareTo(maxPrice) > 0
                || maxPrice.compareTo(BigDecimal.ZERO) < 0 || minPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Price range must not be null, less than 0 or minPrice greater than maxPrice");
        }
        return products.values()
                .stream()
                .filter(product ->
                        product.getPrice().compareTo(minPrice) >= 0
                                && product.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    public List<Product> getProductsSortedByPriceAscending() {
        return products.values()
                .stream()
                .sorted(Comparator
                        .comparing(Product::getPrice))
                .toList();
    }

    public List<Product> getProductsSortedByPriceDescending() {
        return products.values()
                .stream()
                .sorted(Comparator
                        .comparing(Product::getPrice).reversed())
                .toList();
    }

    public List<Product> getProductsSortedByCategoryAndPrice() {
        return products.values()
                .stream()
                .sorted(Comparator
                        .comparing(Product::getCategory)
                        .thenComparing(Product::getPrice))
                .toList();
    }

    public Map<UUID, Product> getAllProducts() {
        return defensiveCopy(products);
    }

    private Map<UUID, Product> defensiveCopy(Map<UUID, Product> products) {
        Map<UUID, Product> copy = new HashMap<>();
        Set<Map.Entry<UUID, Product>> entries = products.entrySet();

        for (Map.Entry<UUID, Product> entry : entries) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    private void checkProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    private void checkId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Product Id cannot be null");
        }
    }
}
