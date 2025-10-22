package com.ordermanagement.orderapp.service;

import com.ordermanagement.orderapp.dto.OrderCreatedDTO;
import com.ordermanagement.orderapp.dto.OrderItemDTO;
import com.ordermanagement.orderapp.dto.OrderResponseDTO;
import com.ordermanagement.orderapp.entity.*;
import com.ordermanagement.orderapp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Creates a new order.
     *
     * @param orderCreateDTO DTO containing order creation data
     * @return OrderResponseDTO with created order information
     * @throws IllegalArgumentException if customer or products not found
     */
    public OrderResponseDTO createOrder(OrderCreatedDTO orderCreateDTO) {
        // Validate customer exists
        Customer customer = customerService.getCustomerById(orderCreateDTO.getCustomerId());

        // Create new order
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.PENDING);

        // Add order items
        for (OrderItemDTO itemDTO : orderCreateDTO.getItems()) {
            Product product = productService.getProductById(itemDTO.getProductId());

            // Validate product is active
            if (!product.getIsActive()) {
                throw new IllegalArgumentException("Product is not active: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPriceFromProduct();

            order.addOrderItem(orderItem);
        }

        // Calculate total amount
        order.calculateTotalAmount();

        // Save order
        Order savedOrder = orderRepository.save(order);

        return OrderResponseDTO.fromEntity(savedOrder);
    }

    /**
     * Retrieves all orders.
     *
     * @return List of all orders as DTOs
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by ID.
     *
     * @param id Order ID
     * @return OrderResponseDTO with order information
     * @throws IllegalArgumentException if order not found
     */
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        return OrderResponseDTO.fromEntity(order);
    }

    /**
     * Retrieves all orders for a specific customer.
     *
     * @param customerId Customer ID
     * @return List of orders for the customer
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of an order.
     *
     * @param id Order ID
     * @param status New order status
     * @return Updated order as DTO
     * @throws IllegalArgumentException if order not found
     */
    public OrderResponseDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return OrderResponseDTO.fromEntity(updatedOrder);
    }

    /**
     * Deletes an order by ID.
     * Only orders with PENDING status can be deleted.
     *
     * @param id Order ID
     * @throws IllegalArgumentException if order not found or cannot be deleted
     */
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot delete order with status: " + order.getStatus());
        }

        orderRepository.deleteById(id);
    }
}
