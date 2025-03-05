package com.swiggy.order.controller;

import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.enums.OrderStatus;
import com.swiggy.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "OrderController", description = "APIs for managing orders")
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order",
            description = "Create a new order for a specific user")
    @Parameter(name = "orderRequestDto", description = "The DTO containing information for creating a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully created a new order",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequestDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input for creating an order")
    })
    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @PathVariable Long userId, @Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(userId, orderRequestDto);
        return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve all orders",
            description = "Retrieve all the orders for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))})
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@Valid @PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getAllOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve an order by ID",
            description = "Retrieve a specific order by its ID for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the order",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@Valid @PathVariable Long userId, @Valid @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(userId, orderId), HttpStatus.OK);
    }

    @Operation(summary = "Update order status",
            description = "Update the status of a specific order for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated the order status"),
            @ApiResponse(responseCode = "404",
                    description = "Order not found")
    })
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@Valid @PathVariable Long userId, @Valid @PathVariable Long orderId) {
        orderService.updateOrderStatus(userId, orderId);
        return new ResponseEntity<>("Order status updated successfully", HttpStatus.OK);
    }
}
