package com.example.loyalty.controller;

import com.example.loyalty.entity.Product;
import com.example.loyalty.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Legacy duplicate product controller kept for reference. Removed Spring MVC
// annotations so it won't be registered as a bean and conflict with the
// namespaced product controller under `com.example.loyalty.product.controller`.
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // PLP - list products with pagination and optional search (q)
    @GetMapping
    public ResponseEntity<Page<Product>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String direction) {
        Page<Product> p = productService.listProducts(page, size, q, sortBy, direction);
        return ResponseEntity.ok(p);
    }

    // PDP - get product details by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // create product (tenant-scoped)
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product p) {
        Product created = productService.createProduct(p);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product p) {
        Product updated = productService.updateProduct(id, p);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
