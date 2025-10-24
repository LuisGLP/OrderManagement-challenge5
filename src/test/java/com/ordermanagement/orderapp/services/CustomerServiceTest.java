package com.ordermanagement.orderapp.services;

import com.ordermanagement.orderapp.entity.Customer;
import com.ordermanagement.orderapp.repository.CustomerRepository;
import com.ordermanagement.orderapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerService
 * Tests business logic in isolation using mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Juan Pérez");
        testCustomer.setEmail("juan.perez@example.com");
        testCustomer.setPhone(3312345678L);
    }

    @Test
    @DisplayName("Should create customer successfully")
    void testCreateCustomer_Success() {
        // Given
        when(customerRepository.existsByEmail(testCustomer.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        Customer result = customerService.createCustomer(testCustomer);

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getName());
        assertEquals("juan.perez@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateCustomer_DuplicateEmail() {
        // Given
        when(customerRepository.existsByEmail(testCustomer.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(testCustomer)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should get customer by ID successfully")
    void testGetCustomerById_Success() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // When
        Customer result = customerService.getCustomerById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getName());
        verify(customerRepository, times(1)).findById(1L);
    }
}
