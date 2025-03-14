package com.swiggy.order.service;

import com.swiggy.order.dto.MenuItemDto;
import com.swiggy.order.dto.OrderLineRequestDto;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.dto.OrderResponseDto;
import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.OrderLine;
import com.swiggy.order.entity.User;
import com.swiggy.order.enums.OrderStatus;
import com.swiggy.order.exceptions.MenuItemNotFoundException;
import com.swiggy.order.exceptions.OrderNotFoundException;
import com.swiggy.order.proxy.CatalogProxyService;
import com.swiggy.order.proxy.FulfillmentProxyService;
import com.swiggy.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository;
    private UserService userService;
    private CatalogProxyService catalogProxyService;
    private FulfillmentProxyService fulfillmentProxyService;
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        orderRepository = mock(OrderRepository.class);
        userService= mock(UserService.class);
        catalogProxyService = mock(CatalogProxyService.class);
        fulfillmentProxyService = mock(FulfillmentProxyService.class);
        orderService = new OrderService(orderRepository, userService, catalogProxyService, fulfillmentProxyService);
    }

    @Test
    void testCreateOrderSuccessfully() {
        Long userId = 1L;
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
                .build();
        MenuItemDto menuItemDto = MenuItemDto.builder()
                .id(1L)
                .name("Burger")
                .price(5.0)
                .build();
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto);

        orderService.createOrder(userId, orderRequestDto);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderFailsWhenMenuItemNotFound() {
        Long userId = 1L;
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
                .build();
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenThrow(new MenuItemNotFoundException("Menu item not found", HttpStatus.NOT_FOUND));

        assertThrows(MenuItemNotFoundException.class, () -> orderService.createOrder(userId, orderRequestDto));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getAllOrdersSuccessfully() {
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        List<OrderLine> firstOrderLines = List.of(
                OrderLine.builder().id(1L).menuItemId(1L).quantity(2).build(),
                OrderLine.builder().id(2L).menuItemId(2L).quantity(3).build()
        );
        List<OrderLine> secondOrderLines = List.of(
                OrderLine.builder().id(3L).menuItemId(3L).menuItemName("Pizza").price(8.0).quantity(1).build(),
                OrderLine.builder().id(4L).menuItemId(4L).menuItemName("Soda").price(2.0).quantity(2).build()
        );
        List<Order> orders = List.of(
                Order.builder().id(1L).restaurantId(1L).user(user).deliveryAddress("123 Main St").orderLines(firstOrderLines).totalPrice(10.0).build(),
                Order.builder().id(2L).restaurantId(2L).user(user).deliveryAddress("456 Elm St").orderLines(secondOrderLines).totalPrice(20.0).build()
        );
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponseDto> result = orderService.getAllOrders(userId);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderId());
        assertEquals(2L, result.get(1).getOrderId());
        assertEquals(2, result.get(0).getOrderLines().size());
        assertEquals(2, result.get(1).getOrderLines().size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrdersWhenNoOrdersAvailable() {
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = orderService.getAllOrders(userId);

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderByIdSuccessfully() {
        Long userId = 1L;
        Long orderId = 1L;
        User user = User.builder().id(userId).build();
        Order order = Order.builder()
                .id(orderId)
                .restaurantId(1L)
                .user(user)
                .deliveryAddress("123 Main St")
                .orderLines(List.of(
                        OrderLine.builder().id(1L).menuItemId(1L).menuItemName("Burger").price(5.0).quantity(2).build(),
                        OrderLine.builder().id(2L).menuItemId(2L).menuItemName("Fries").price(3.0).quantity(1).build()
                ))
                .totalPrice(13.0)
                .build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.of(order));

        OrderResponseDto result = orderService.getOrderById(userId, orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals("123 Main St", result.getDeliveryAddress());
        assertEquals(2, result.getOrderLines().size());
        verify(orderRepository, times(1)).findByIdAndUser(orderId, user);
    }

    @Test
    void getOrderByIdNotFound() {
        Long userId = 1L;
        Long orderId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(userId, orderId));
        verify(orderRepository, times(1)).findByIdAndUser(orderId, user);
    }

    @Test
    void updateOrderStatusSuccessfully() {
        Long userId = 1L;
        Long orderId = 1L;
        User user = User.builder().id(userId).build();
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatus.CREATED)
                .user(user)
                .build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(userId, orderId);

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, order.getStatus());
    }

    @Test
    void updateOrderStatusNotFound() {
        Long userId = 1L;
        Long orderId = 1L;
        User user = User.builder().id(userId).build();
        when(userService.fetchUser(userId)).thenReturn(user);
        when(orderRepository.findByIdAndUser(orderId, user)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(userId, orderId));
    }

}
