package com.swiggy.order.service;

import com.swiggy.order.dto.MenuItemDto;
import com.swiggy.order.dto.OrderLineRequestDto;
import com.swiggy.order.dto.OrderRequestDto;
import com.swiggy.order.entity.Order;
import com.swiggy.order.exceptions.MenuItemNotFoundException;
import com.swiggy.order.proxy.CatalogProxyService;
import com.swiggy.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository;
    private CatalogProxyService catalogProxyService;
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        orderRepository = mock(OrderRepository.class);
        catalogProxyService = mock(CatalogProxyService.class);
        orderService = new OrderService(orderRepository, catalogProxyService);
    }

    @Test
    void testCreateOrderSuccessfully() {
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
                .build();
        MenuItemDto menuItemDto = MenuItemDto.builder()
                .id(1L)
                .name("Burger")
                .price(5.0)
                .build();
        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto);

        orderService.createOrder(orderRequestDto);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderFailsWhenMenuItemNotFound() {
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .restaurantId(1L)
                .customerId(1L)
                .deliveryAddress("123 Main St")
                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
                .build();
        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenThrow(new MenuItemNotFoundException("Menu item not found", HttpStatus.NOT_FOUND));

        assertThrows(MenuItemNotFoundException.class, () -> orderService.createOrder(orderRequestDto));

        verify(orderRepository, never()).save(any(Order.class));
    }

}
