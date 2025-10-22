package com.ordermanagement.orderapp.controller;

import com.ordermanagement.orderapp.dto.OrderCreatedDTO;
import com.ordermanagement.orderapp.dto.OrderResponseDTO;
import com.ordermanagement.orderapp.entity.Order;
import com.ordermanagement.orderapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order.
     *
     * @param orderCreatedDTO Order creation data including customer and items
     * @return Created order with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreatedDTO orderCreatedDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderCreatedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Retrieves all orders.
     *
     * @return List of all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves an order by ID.
     *
     * @param id Order ID
     * @return Order data with all details
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Retrieves all orders for a specific customer.
     *
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates the status of an order.
     *
     * @param id Order ID
     * @param status New order status
     * @return Updated order
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Deletes an order.
     * Only orders with PENDING or CANCELLED status can be deleted.
     *
     * @param id Order ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}