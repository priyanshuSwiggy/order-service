package com.swiggy.order.service;

import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    public void createOrder(OrderRequestDto orderRequestDto) {
    }

    public List<OrderResponseDto> getAllOrders() {
        return null;
    }

    public OrderResponseDto getOrderById(Long orderId) {
        return null;
    }

    public void updateOrderStatus(Long orderId) {
    }
}
