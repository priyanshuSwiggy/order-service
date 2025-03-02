package com.swiggy.order.service;

import com.swiggy.order.dto.MenuItemDto;
import com.swiggy.order.dto.OrderLineRequestDto;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.OrderLine;
import com.swiggy.order.entity.User;
import com.swiggy.order.enums.OrderStatus;
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
    private final UserService userService;
    private final CatalogProxyService catalogProxyService;

    public void createOrder(Long userId, OrderRequestDto orderRequestDto) {
        User user = userService.fetchUser(userId);
        List<OrderLineRequestDto> orderLineRequests = orderRequestDto.getOrderLines();
        List<OrderLine> orderLines = orderLineRequests.stream().map(item -> {
            MenuItemDto menuItemDto = catalogProxyService.getMenuItemByIdAndRestaurantId(orderRequestDto.getRestaurantId(), item.getMenuItemId());
            return OrderLine.builder().menuItemId(item.getMenuItemId()).menuItemName(menuItemDto.getName()).quantity(item.getQuantity()).price(menuItemDto.getPrice()).currency(menuItemDto.getCurrency()).build();
        }).toList();
        double totalPrice = orderLines.stream()
                .mapToDouble(orderLine -> orderLine.getPrice() * orderLine.getQuantity())
                .sum();
        Order order = Order.builder()
                .restaurantId(orderRequestDto.getRestaurantId())
                .totalPrice(totalPrice)
                .deliveryAddress(orderRequestDto.getDeliveryAddress())
                .status(OrderStatus.CREATED)
                .orderLines(orderLines)
                .user(user)
                .build();
        orderRepository.save(order);
    }

    public List<OrderResponseDto> getAllOrders(Long userId) {
        userService.fetchUser(userId);
        return orderRepository.findAll().stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        Order order = fetchOrder(userId, orderId);
        System.out.println(order.getOrderLines());
        return new OrderResponseDto(order);
    }

    public void updateOrderStatus(Long userId, Long orderId, OrderStatus orderStatus) {
        Order order = fetchOrder(userId, orderId);
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    private Order fetchOrder(Long userId, Long orderId) {
        User user = userService.fetchUser(userId);
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND));
    }
}
