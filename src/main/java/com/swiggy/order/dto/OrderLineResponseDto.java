package com.swiggy.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineResponseDto {
    private Long menuItemId;
    private String menuItemName;
    private double price;
    private int quantity;
}
