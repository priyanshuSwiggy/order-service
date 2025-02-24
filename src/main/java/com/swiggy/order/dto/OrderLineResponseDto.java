package com.swiggy.order.dto;

import com.swiggy.order.entity.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderLineResponseDto {
    private Long orderLineId;
    private Long menuItemId;
    private String menuItemName;
    private double price;
    private int quantity;

    public OrderLineResponseDto(OrderLine orderLine) {
        this.orderLineId = orderLine.getId();
        this.menuItemId = orderLine.getMenuItemId();
        this.menuItemName = orderLine.getMenuItemName();
        this.price = orderLine.getPrice();
        this.quantity = orderLine.getQuantity();
    }
}
