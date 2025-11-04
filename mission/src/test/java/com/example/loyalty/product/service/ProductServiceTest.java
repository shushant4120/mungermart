package com.example.loyalty.product.service;

import com.example.loyalty.entity.Product;
import com.example.loyalty.multitenant.TenantContext;
import com.example.loyalty.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ProductServiceTest {

    @Mock
    ProductRepository productRepo;

    @InjectMocks
    ProductService productService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        TenantContext.setTenantId("T1");
    }

    @Test
    void listProducts_noQuery_returnsPage() {
        Product p = new Product();
        p.id = "p1";
        p.name = "Tomato";
        p.tenantId = "T1";
        @SuppressWarnings("unchecked")
        Page<Product> page = new PageImpl<>((java.util.List<Product>) java.util.Arrays.asList(p)); // Java 8
                                                                                                   // compatibility
        when(productRepo.findByTenantId(ArgumentMatchers.eq("T1"), ArgumentMatchers.any())).thenReturn(page);

        Page<Product> res = productService.listProducts(0, 10, null, null, "ASC");
        assertNotNull(res);
        assertEquals(1, res.getTotalElements());
        verify(productRepo, times(1)).findByTenantId(eq("T1"), any());
    }

    @Test
    void getProduct_found_returnsOptional() {
        Product p = new Product();
        p.id = "p1";
        p.name = "Tomato";
        p.tenantId = "T1";
        when(productRepo.findByTenantIdAndId("T1", "p1")).thenReturn(Optional.of(p));

        Optional<Product> res = productService.getProduct("p1");
        assertTrue(res.isPresent());
        assertEquals("Tomato", res.get().name);
    }

    @Test
    void createProduct_setsTenantAndSaves() {
        Product p = new Product();
        p.name = "Carrot";
        Product saved = new Product();
        saved.id = "p2";
        saved.name = "Carrot";
        saved.tenantId = "T1";
        when(productRepo.save(ArgumentMatchers.any())).thenReturn(saved);

        Product res = productService.createProduct(p);
        assertEquals("T1", res.tenantId);
        assertEquals("p2", res.id);
    }

    @Test
    void updateProduct_missing_throws() {
        when(productRepo.findByTenantIdAndId("T1", "nope")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.updateProduct("nope", new Product()));
    }

    @Test
    void deleteProduct_missing_throws() {
        when(productRepo.findByTenantIdAndId("T1", "nope")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.deleteProduct("nope"));
    }
}
