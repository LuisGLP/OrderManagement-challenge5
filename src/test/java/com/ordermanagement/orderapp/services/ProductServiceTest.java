package com.ordermanagement.orderapp.services;

import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.repository.ProductRepository;
import com.ordermanagement.orderapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService
 * Tests business logic in isolation using mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop Dell XPS 15");
        testProduct.setDescription("High performance laptop");
        testProduct.setPrice(new BigDecimal("25999.99"));
        testProduct.setIsActive(true);
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct_Success() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.createProduct(testProduct);

        // Then
        assertNotNull(result);
        assertEquals("Laptop Dell XPS 15", result.getName());
        assertEquals(new BigDecimal("25999.99"), result.getPrice());
        assertTrue(result.getIsActive());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should get all active products")
    void testGetActiveProducts_Success() {
        // Given
        Product product1 = new Product();
        product1.setIsActive(true);
        Product product2 = new Product();
        product2.setIsActive(true);

        when(productRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productService.getActiveProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Product::getIsActive));
        verify(productRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct_Success() {
        // Given
        Product updatedProduct = new Product();
        updatedProduct.setName("Laptop Dell XPS 15 Updated");
        updatedProduct.setPrice(new BigDecimal("24999.99"));
        updatedProduct.setIsActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.updateProduct(1L, updatedProduct);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
