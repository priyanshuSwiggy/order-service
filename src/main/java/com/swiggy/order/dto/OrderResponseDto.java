package com.swiggy.order.dto;

import com.swiggy.order.entity.OrderLine;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private Long orderId;

    private Long restaurantId;

    private Long customerId;

    private String deliveryAddress;

    private List<OrderLineResponseDto> orderLines;
}