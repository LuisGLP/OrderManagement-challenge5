package com.ordermanagement.orderapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.orderapp.entity.Customer;
import com.ordermanagement.orderapp.repository.CustomerRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerController
 * Tests the full stack with real database
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Customer Controller Integration Tests")
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create customer via API")
    void testCreateCustomer_Integration() throws Exception {
        // Given
        Customer customer = new Customer();
        customer.setName("Integration Test Customer");
        customer.setEmail("integration@test.com");
        customer.setPhone(3312345678L);

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test Customer"))
                .andExpect(jsonPath("$.email").value("integration@test.com"));
    }

    @Test
    @DisplayName("Should get customer by ID via API")
    void testGetCustomerById_Integration() throws Exception {
        // Given
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone(3312345678L);
        Customer saved = customerRepository.save(customer);

        // When & Then
        mockMvc.perform(get("/api/customers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Customer"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should return 400 when creating customer with duplicate email")
    void testCreateCustomer_DuplicateEmail_Integration() throws Exception {
        // Given - Create first customer
        Customer customer = new Customer();
        customer.setName("First Customer");
        customer.setEmail("duplicate@test.com");
        customer.setPhone(3312345678L);
        customerRepository.save(customer);

        // When - Try to create second customer with same email
        Customer duplicateCustomer = new Customer();
        duplicateCustomer.setName("Second Customer");
        duplicateCustomer.setEmail("duplicate@test.com");
        duplicateCustomer.setPhone(3398765432L);

        // Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateCustomer)))
                .andExpect(status().isBadRequest());
    }
}
