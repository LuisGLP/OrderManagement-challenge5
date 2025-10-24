package com.ordermanagement.orderapp.services;

import com.ordermanagement.orderapp.dto.OrderCreatedDTO;
import com.ordermanagement.orderapp.dto.OrderItemDTO;
import com.ordermanagement.orderapp.dto.OrderResponseDTO;
import com.ordermanagement.orderapp.entity.Customer;
import com.ordermanagement.orderapp.entity.Order;
import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.repository.OrderRepository;
import com.ordermanagement.orderapp.service.CustomerService;
import com.ordermanagement.orderapp.service.OrderService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService
 * Tests business logic in isolation using mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Customer testCustomer;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Juan Pérez");
        testCustomer.setEmail("juan.perez@example.com");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop");
        testProduct.setPrice(new BigDecimal("25999.99"));
        testProduct.setIsActive(true);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomer(testCustomer);
        testOrder.setStatus(Order.OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Should create order successfully")
    void testCreateOrder_Success() {
        // Given
        OrderItemDTO itemDTO = new OrderItemDTO(1L, 2);
        OrderCreatedDTO createDTO = new OrderCreatedDTO(1L, Arrays.asList(itemDTO));

        when(customerService.getCustomerById(1L)).thenReturn(testCustomer);
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponseDTO result = orderService.createOrder(createDTO);

        // Then
        assertNotNull(result);
        verify(customerService, times(1)).getCustomerById(1L);
        verify(productService, times(1)).getProductById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when product is not active")
    void testCreateOrder_InactiveProduct() {
        // Given
        testProduct.setIsActive(false);
        OrderItemDTO itemDTO = new OrderItemDTO(1L, 2);
        OrderCreatedDTO createDTO = new OrderCreatedDTO(1L, Arrays.asList(itemDTO));

        when(customerService.getCustomerById(1L)).thenReturn(testCustomer);
        when(productService.getProductById(1L)).thenReturn(testProduct);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(createDTO)
        );

        assertTrue(exception.getMessage().contains("not active"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should update order status successfully")
    void testUpdateOrderStatus_Success() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponseDTO result = orderService.updateOrderStatus(1L, Order.OrderStatus.CONFIRMED);

        // Then
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}