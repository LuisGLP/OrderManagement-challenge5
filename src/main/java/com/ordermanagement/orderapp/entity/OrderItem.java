package com.ordermanagement.orderapp.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entity class representing an OrderItem in the Order Management System.
 * An order item links a product to an order with a specified quantity.
 *
 * @author Luis Garcia
 * @version 0.1
 */

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_itemId;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public BigDecimal getSubtotal() {
        if (unitPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void setUnitPriceFromProduct() {
        if (product != null) {
            this.unitPrice = product.getPrice();
        }

    }

    @Transient
    public Long getId() {
        return this.order_itemId;
    }
}

