package com.swiggy.order.dto;

import com.swiggy.order.entity.OrderLine;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequestDto {
    @NotNull(message = "Restaurant Id cannot be null")
    @Positive(message = "Restaurant Id should be positive")
    private Long restaurantId;

    @NotNull(message = "Customer Id cannot be null")
    @Positive(message = "Customer Id should be positive")
    private Long customerId;

    @NotBlank(message = "Delivery address cannot be null or empty")
    @Size(max = 255, message = "Delivery address cannot exceed 255 characters")
    private String deliveryAddress;

    @NotNull(message = "Order lines cannot be null")
    private List<OrderLine> orderLines;
}