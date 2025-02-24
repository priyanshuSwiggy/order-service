package com.swiggy.order.service;

import com.swiggy.order.dto.MenuItemDto;
import com.swiggy.order.dto.OrderLineRequestDto;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.OrderLine;
import com.swiggy.order.exceptions.OrderNotFoundException;
import com.swiggy.order.proxy.CatalogProxyService;
import com.swiggy.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CatalogProxyService catalogProxyService;

    public void createOrder(OrderRequestDto orderRequestDto) {
        List<OrderLineRequestDto> orderLineRequests = orderRequestDto.getOrderLines();
        List<OrderLine> orderLines = orderLineRequests.stream().map(item -> {
            MenuItemDto menuItemDto = catalogProxyService.getMenuItemByIdAndRestaurantId(orderRequestDto.getRestaurantId(), item.getMenuItemId());
            return OrderLine.builder().menuItemId(item.getMenuItemId()).menuItemName(menuItemDto.getName()).quantity(item.getQuantity()).price(menuItemDto.getPrice()).build();
        }).toList();
        double totalPrice = orderLines.stream()
                .mapToDouble(orderLine -> orderLine.getPrice() * orderLine.getQuantity())
                .sum();
        Order order = Order.builder()
                .restaurantId(orderRequestDto.getRestaurantId())
                .customerId(orderRequestDto.getCustomerId())
                .totalPrice(totalPrice)
                .deliveryAddress(orderRequestDto.getDeliveryAddress())
                .orderLines(orderLines)
                .build();
        orderRepository.save(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    public OrderResponseDto getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderResponseDto::new)
                .orElseThrow(() -> new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND));
    }

    public void updateOrderStatus(Long orderId) {
    }
}
