package com.swiggy.order.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineRequestDto {
    @NotNull(message = "MenuItem Id cannot be null")
    @Positive(message = "MenuItem Id should be positive")
    private Long menuItemId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity should be positive")
    private int quantity;
}
