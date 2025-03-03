package com.swiggy.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.enums.OrderStatus;
import com.swiggy.order.exceptions.GlobalExceptionHandler;
import com.swiggy.order.exceptions.OrderNotFoundException;
import com.swiggy.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
    public static final String ORDERS_URL = "/users/{userId}/orders";
    public static final String SPECIFIC_ORDER_URL = "/users/{userId}/orders/{orderId}";
    public static final String UPDATE_ORDER_STATUS_URL = "/users/{userId}/orders/{orderId}/status";

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testCreateOrderSuccessfully() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();
        doNothing().when(orderService).createOrder(userId, orderRequestDto);

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order created successfully"));
    }

    @Test
    void testCreateOrderValidationFailsWhenRestaurantIdIsNull() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(null)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id cannot be null\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenRestaurantIdIsZero() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(0L)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id should be positive\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenRestaurantIdIsNegative() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(-1L)
                
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id should be positive\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenDeliveryAddressIsNull() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"deliveryAddress\":\"Delivery address cannot be null or empty\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenDeliveryAddressIsEmpty() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                
                .deliveryAddress("")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"deliveryAddress\":\"Delivery address cannot be null or empty\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenOrderLinesIsNull() throws Exception {
        final Long userId = 1L;
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                
                .deliveryAddress("123 Main St")
                .build();

        mockMvc.perform(post(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"orderLines\":\"Order lines cannot be null\"}"));
    }

    @Test
    void testGetAllOrdersSuccessfully() throws Exception {
        final Long userId = 1L;
        final List<OrderResponseDto> orders = List.of(
                OrderResponseDto.builder().restaurantId(1L).deliveryAddress("123 Main St").orderLines(Collections.emptyList()).build(),
                OrderResponseDto.builder().restaurantId(2L).deliveryAddress("456 Elm St").orderLines(Collections.emptyList()).build()
        );
        when(orderService.getAllOrders(userId)).thenReturn(orders);

        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orders)))
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(orders);
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetAllOrdersWhenNoOrdersAreAvailable() throws Exception {
        final Long userId = 1L;
        when(orderService.getAllOrders(userId)).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();

        String expectedResponse = "[]";
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetOrderByIdSuccessfully() throws Exception {
        final Long userId = 1L;
        final Long orderId = 1L;
        final OrderResponseDto order = OrderResponseDto.builder()
                .restaurantId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();
        when(orderService.getOrderById(1L, 1L)).thenReturn(order);

        MvcResult mvcResult = mockMvc.perform(get(SPECIFIC_ORDER_URL, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)))
                .andReturn();

        String expectedResponse = objectMapper.writeValueAsString(order);
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        final Long userId = 1L;
        final Long orderId = 1L;
        when(orderService.getOrderById(userId, orderId)).thenThrow(new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get(SPECIFIC_ORDER_URL, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    void testGetOrderByIdInvalidId() throws Exception {
        final Long userId = 1L;
        mockMvc.perform(get(SPECIFIC_ORDER_URL, userId, "invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateOrderStatusSuccessfully() throws Exception {
        final Long userId = 1L;
        final Long orderId = 1L;
        doNothing().when(orderService).updateOrderStatus(userId, orderId);

        mockMvc.perform(put(UPDATE_ORDER_STATUS_URL, userId, orderId)
                        .param("orderStatus", OrderStatus.OUT_FOR_DELIVERY.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated successfully"));
    }

    @Test
    void testUpdateOrderStatusNotFound() throws Exception {
        final Long userId = 1L;
        final Long orderId = 1L;
        doThrow(new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND)).when(orderService).updateOrderStatus(userId, orderId);

        mockMvc.perform(put(UPDATE_ORDER_STATUS_URL, userId, orderId)
                        .param("orderStatus", OrderStatus.OUT_FOR_DELIVERY.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }
}
