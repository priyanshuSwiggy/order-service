package com.swiggy.order.dto;

import com.swiggy.order.entity.Order;
import com.swiggy.order.enums.OrderStatus;
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
    private double totalPrice;
    private String deliveryAddress;
    private OrderStatus orderStatus;
    private List<OrderLineResponseDto> orderLines;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.restaurantId = order.getRestaurantId();
        this.totalPrice = order.getTotalPrice();
        this.deliveryAddress = order.getDeliveryAddress();
        this.orderStatus = order.getStatus();
        this.orderLines = order.getOrderLines().stream().map(OrderLineResponseDto::new).toList();
    }
}