package com.swiggy.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.exceptions.GlobalExceptionHandler;
import com.swiggy.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
    public static final String ORDERS_URL = "/orders";
    public static final String SPECIFIC_ORDER_URL = "/orders/{orderId}";

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
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();
        doNothing().when(orderService).createOrder(orderRequestDto);

        mockMvc.perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order created successfully"));
    }

    @Test
    void testCreateOrderValidationFailsWhenRestaurantIdIsNull() throws Exception {
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(null)
                .customerId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id cannot be null\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenCustomerIdIsNull() throws Exception {
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(null)
                .deliveryAddress("123 Main St")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"customerId\":\"Customer Id cannot be null\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenDeliveryAddressIsEmpty() throws Exception {
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(1L)
                .deliveryAddress("")
                .orderLines(Collections.emptyList())
                .build();

        mockMvc.perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"deliveryAddress\":\"Delivery address cannot be empty\"}"));
    }

    @Test
    void testCreateOrderValidationFailsWhenOrderLinesIsNull() throws Exception {
        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(1L)
                .deliveryAddress("123 Main St")
                .build();

        mockMvc.perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"orderLines\":\"Order lines cannot be null\"}"));
    }

    @Test
    void testGetAllOrdersSuccessfully() throws Exception {
        final List<OrderResponseDto> orders = List.of(
                OrderResponseDto.builder().restaurantId(1L).customerId(1L).deliveryAddress("123 Main St").orderLines(Collections.emptyList()).build(),
                OrderResponseDto.builder().restaurantId(2L).customerId(2L).deliveryAddress("456 Elm St").orderLines(Collections.emptyList()).build()
        );
        when(orderService.getAllOrders()).thenReturn(orders);

        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL)
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
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();

        String expectedResponse = "[]";
        String actualResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }
}
