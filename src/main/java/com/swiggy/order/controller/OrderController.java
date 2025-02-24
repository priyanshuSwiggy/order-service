package com.swiggy.order.controller;

import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@Valid @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(orderId), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@Valid @PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId);
        return new ResponseEntity<>("Order status updated successfully", HttpStatus.OK);
    }
}
