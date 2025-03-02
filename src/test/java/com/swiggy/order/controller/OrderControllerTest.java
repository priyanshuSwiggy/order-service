//package com.swiggy.order.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.swiggy.order.dto.OrderRequestDto;
//import com.swiggy.order.dto.OrderResponseDto;
//import com.swiggy.order.enums.OrderStatus;
//import com.swiggy.order.exceptions.GlobalExceptionHandler;
//import com.swiggy.order.exceptions.OrderNotFoundException;
//import com.swiggy.order.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class OrderControllerTest {
//    public static final String ORDERS_URL = "/orders";
//    public static final String SPECIFIC_ORDER_URL = "/orders/{orderId}";
//    public static final String UPDATE_ORDER_STATUS_URL = "/orders/{orderId}/status";
//
//    @Mock
//    private OrderService orderService;
//
//    @InjectMocks
//    private OrderController orderController;
//
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
//                .setControllerAdvice(new GlobalExceptionHandler()).build();
//    }
//
//    @Test
//    void testCreateOrderSuccessfully() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//        doNothing().when(orderService).createOrder(orderRequestDto);
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Order created successfully"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenRestaurantIdIsNull() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(null)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id cannot be null\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenRestaurantIdIsZero() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(0L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id should be positive\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenRestaurantIdIsNegative() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(-1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"restaurantId\":\"Restaurant Id should be positive\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenUserIdIsNull() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(null)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"userId\":\"User Id cannot be null\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenUserIdIsZero() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(0L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"userId\":\"User Id should be positive\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenUserIdIsNegative() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(-1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"userId\":\"User Id should be positive\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenDeliveryAddressIsNull() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"deliveryAddress\":\"Delivery address cannot be null or empty\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenDeliveryAddressIsEmpty() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("")
//                .orderLines(Collections.emptyList())
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"deliveryAddress\":\"Delivery address cannot be null or empty\"}"));
//    }
//
//    @Test
//    void testCreateOrderValidationFailsWhenOrderLinesIsNull() throws Exception {
//        final OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .build();
//
//        mockMvc.perform(post(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json("{\"orderLines\":\"Order lines cannot be null\"}"));
//    }
//
//    @Test
//    void testGetAllOrdersSuccessfully() throws Exception {
//        final List<OrderResponseDto> orders = List.of(
//                OrderResponseDto.builder().restaurantId(1L).userId(1L).deliveryAddress("123 Main St").orderLines(Collections.emptyList()).build(),
//                OrderResponseDto.builder().restaurantId(2L).userId(2L).deliveryAddress("456 Elm St").orderLines(Collections.emptyList()).build()
//        );
//        when(orderService.getAllOrders()).thenReturn(orders);
//
//        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(orders)))
//                .andReturn();
//
//        String expectedResponse = objectMapper.writeValueAsString(orders);
//        String actualResponse = mvcResult.getResponse().getContentAsString();
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testGetAllOrdersWhenNoOrdersAreAvailable() throws Exception {
//        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());
//
//        MvcResult mvcResult = mockMvc.perform(get(ORDERS_URL)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"))
//                .andReturn();
//
//        String expectedResponse = "[]";
//        String actualResponse = mvcResult.getResponse().getContentAsString();
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testGetOrderByIdSuccessfully() throws Exception {
//        final OrderResponseDto order = OrderResponseDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(Collections.emptyList())
//                .build();
//        when(orderService.getOrderById(1L)).thenReturn(order);
//
//        MvcResult mvcResult = mockMvc.perform(get(SPECIFIC_ORDER_URL, 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(order)))
//                .andReturn();
//
//        String expectedResponse = objectMapper.writeValueAsString(order);
//        String actualResponse = mvcResult.getResponse().getContentAsString();
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void testGetOrderByIdNotFound() throws Exception {
//        when(orderService.getOrderById(1L)).thenThrow(new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND));
//
//        mockMvc.perform(get(SPECIFIC_ORDER_URL, 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Order not found"));
//    }
//
//    @Test
//    void testGetOrderByIdInvalidId() throws Exception {
//        mockMvc.perform(get(SPECIFIC_ORDER_URL, "invalidId")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testUpdateOrderStatusSuccessfully() throws Exception {
//        doNothing().when(orderService).updateOrderStatus(1L, OrderStatus.DELIVERED);
//
//        mockMvc.perform(put(UPDATE_ORDER_STATUS_URL, 1L)
//                        .param("orderStatus", OrderStatus.DELIVERED.name())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Order status updated successfully"));
//    }
//
//    @Test
//    void testUpdateOrderStatusNotFound() throws Exception {
//        doThrow(new OrderNotFoundException("Order not found", HttpStatus.NOT_FOUND)).when(orderService).updateOrderStatus(1L, OrderStatus.DELIVERED);
//
//        mockMvc.perform(put(UPDATE_ORDER_STATUS_URL, 1L)
//                        .param("orderStatus", OrderStatus.DELIVERED.name())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Order not found"));
//    }
//}
