package com.ordermanagement.orderapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.orderapp.dto.OrderCreatedDTO;
import com.ordermanagement.orderapp.dto.OrderItemDTO;
import com.ordermanagement.orderapp.entity.Customer;
import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.repository.CustomerRepository;
import com.ordermanagement.orderapp.repository.OrderRepository;
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
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for OrderController
 * Tests the full stack with real database
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Order Controller Integration Tests")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Customer testCustomer;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();

        // Create test customer
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("testorder@example.com");
        testCustomer.setPhone(3312345678L);
        testCustomer = customerRepository.save(testCustomer);

        // Create test product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setIsActive(true);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Should create order via API")
    void testCreateOrder_Integration() throws Exception {
        // Given
        OrderItemDTO itemDTO = new OrderItemDTO(testProduct.getId(), 2);
        OrderCreatedDTO createDTO = new OrderCreatedDTO(testCustomer.getId(), Arrays.asList(itemDTO));

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Test Customer"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should get all orders via API")
    void testGetAllOrders_Integration() throws Exception {
        // Given - Create an order first
        OrderItemDTO itemDTO = new OrderItemDTO(testProduct.getId(), 1);
        OrderCreatedDTO createDTO = new OrderCreatedDTO(testCustomer.getId(), Arrays.asList(itemDTO));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());

        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Should return 400 when creating order with inactive product")
    void testCreateOrder_InactiveProduct_Integration() throws Exception {
        // Given
        testProduct.setIsActive(false);
        productRepository.save(testProduct);

        OrderItemDTO itemDTO = new OrderItemDTO(testProduct.getId(), 1);
        OrderCreatedDTO createDTO = new OrderCreatedDTO(testCustomer.getId(), Arrays.asList(itemDTO));

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }
}
