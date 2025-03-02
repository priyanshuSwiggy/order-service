package com.swiggy.order.controller;

import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.enums.OrderStatus;
import com.swiggy.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @PathVariable Long userId, @Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(userId, orderRequestDto);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@Valid @PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getAllOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@Valid @PathVariable Long userId, @Valid @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(userId, orderId), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@Valid @PathVariable Long userId, @Valid @PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
        orderService.updateOrderStatus(userId, orderId, orderStatus);
        return new ResponseEntity<>("Order status updated successfully", HttpStatus.OK);
    }
}
