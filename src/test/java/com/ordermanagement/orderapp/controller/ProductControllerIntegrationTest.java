package com.ordermanagement.orderapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ProductController
 * Tests the full stack with real database
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Product Controller Integration Tests")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create product via API")
    void testCreateProduct_Integration() throws Exception {
        // Given
        Product product = new Product();
        product.setName("Test Laptop");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("25999.99"));
        product.setIsActive(true);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Laptop"))
                .andExpect(jsonPath("$.price").value(25999.99));
    }

    @Test
    @DisplayName("Should get all products via API")
    void testGetAllProducts_Integration() throws Exception {
        // Given
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal("100.00"));
        product1.setIsActive(true);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal("200.00"));
        product2.setIsActive(true);

        productRepository.save(product1);
        productRepository.save(product2);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should update product via API")
    void testUpdateProduct_Integration() throws Exception {
        // Given
        Product product = new Product();
        product.setName("Original Product");
        product.setPrice(new BigDecimal("1000.00"));
        product.setIsActive(true);
        Product saved = productRepository.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(new BigDecimal("1500.00"));
        updatedProduct.setIsActive(true);

        // When & Then
        mockMvc.perform(put("/api/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(1500.00));
    }
}
