package com.example.loyalty.product.controller;

import com.example.loyalty.entity.Product;
import com.example.loyalty.product.service.ProductService;
import com.example.loyalty.security.RequireRole;
import com.example.loyalty.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // PLP - list products with pagination and optional search (q)
    @GetMapping("/list-products")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String direction) {
        Page<Product> p = productService.listProducts(page, size, q, sortBy, direction);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return productService.getProduct(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create-product")
    @RequireRole({ Role.MERCHANT, Role.ADMIN })
    public ResponseEntity<Product> createProduct(@RequestBody Product p) {
        Product created = productService.createProduct(p);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update-product/{id}")
    @RequireRole({ Role.MERCHANT, Role.ADMIN })
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product p) {
        Product updated = productService.updateProduct(id, p);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete-product/{id}")
    @RequireRole({ Role.ADMIN })
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
