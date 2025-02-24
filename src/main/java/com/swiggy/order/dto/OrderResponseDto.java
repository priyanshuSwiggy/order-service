package com.swiggy.order.dto;

import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long restaurantId;
    private Long customerId;
    private String deliveryAddress;
    private List<OrderLineResponseDto> orderLines;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.restaurantId = order.getRestaurantId();
        this.customerId = order.getCustomerId();
        this.deliveryAddress = order.getDeliveryAddress();
        this.orderLines = order.getOrderLines().stream().map(OrderLineResponseDto::new).toList();
    }
}