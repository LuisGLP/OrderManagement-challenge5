package com.ordermanagement.orderapp.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @NotNull(message = "Product ID is required")
    private Long id;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity is required")
    private Integer quantity;

}
