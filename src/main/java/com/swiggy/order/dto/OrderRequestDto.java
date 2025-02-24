package com.swiggy.order.dto;

import com.swiggy.order.entity.OrderLine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequestDto {
    @NotNull(message = "Restaurant Id cannot be null")
    private Long restaurantId;

    @NotNull(message = "Customer Id cannot be null")
    private Long customerId;

    @NotEmpty(message = "Delivery address cannot be empty")
    @Size(max = 255, message = "Delivery address cannot exceed 255 characters")
    private String deliveryAddress;

    @NotNull(message = "Order lines cannot be null")
    private List<OrderLine> orderLines;
}