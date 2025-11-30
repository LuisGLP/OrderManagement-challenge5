package com.ordermanagement.orderapp.controller;

import com.ordermanagement.orderapp.dto.OrderCreatedDTO;
import com.ordermanagement.orderapp.dto.OrderResponseDTO;
import com.ordermanagement.orderapp.entity.Order;
import com.ordermanagement.orderapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "CRUD for orders - Main resource for MELI")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order.
     *
     * @param orderCreatedDTO Order creation data including customer and items
     * @return Created order with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new order",
            description = "Creates a new order with the specified items for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or inactive product"),
            @ApiResponse(responseCode = "404", description = "Customer or product not found")
    })
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
    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
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
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
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
    @Operation(summary = "Get orders by customer",
            description = "Retrieves all orders placed by a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
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
    @Operation(summary = "Update order status",
            description = "Updates the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @Parameter(description = "New order status (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)")
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
    @Operation(summary = "Delete an order",
            description = "Deletes an order (only PENDING or CANCELLED orders can be deleted)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Cannot delete order with current status")
    })
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}