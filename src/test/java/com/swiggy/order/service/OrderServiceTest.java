//package com.swiggy.order.service;
//
//import com.swiggy.order.dto.MenuItemDto;
//import com.swiggy.order.dto.OrderLineRequestDto;
//import com.swiggy.order.dto.OrderRequestDto;
//import com.swiggy.order.dto.OrderResponseDto;
//import com.swiggy.order.entity.Order;
//import com.swiggy.order.entity.OrderLine;
//import com.swiggy.order.enums.OrderStatus;
//import com.swiggy.order.exceptions.MenuItemNotFoundException;
//import com.swiggy.order.exceptions.OrderNotFoundException;
//import com.swiggy.order.proxy.CatalogProxyService;
//import com.swiggy.order.repository.OrderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class OrderServiceTest {
//
//    private OrderRepository orderRepository;
//    private CatalogProxyService catalogProxyService;
//    private OrderService orderService;
//
//    @BeforeEach
//    public void setup() {
//        orderRepository = mock(OrderRepository.class);
//        catalogProxyService = mock(CatalogProxyService.class);
//        orderService = new OrderService(orderRepository, catalogProxyService);
//    }
//
//    @Test
//    void testCreateOrderSuccessfully() {
//        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
//                .build();
//        MenuItemDto menuItemDto = MenuItemDto.builder()
//                .id(1L)
//                .name("Burger")
//                .price(5.0)
//                .build();
//        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto);
//
//        orderService.createOrder(orderRequestDto);
//
//        verify(orderRepository, times(1)).save(any(Order.class));
//    }
//
//    @Test
//    void testCreateOrderFailsWhenMenuItemNotFound() {
//        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(List.of(OrderLineRequestDto.builder().menuItemId(1L).quantity(2).build()))
//                .build();
//        when(catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenThrow(new MenuItemNotFoundException("Menu item not found", HttpStatus.NOT_FOUND));
//
//        assertThrows(MenuItemNotFoundException.class, () -> orderService.createOrder(orderRequestDto));
//
//        verify(orderRepository, never()).save(any(Order.class));
//    }
//
//    @Test
//    void getAllOrdersSuccessfully() {
//        List<OrderLine> firstOrderLines = List.of(
//                OrderLine.builder().id(1L).menuItemId(1L).quantity(2).build(),
//                OrderLine.builder().id(2L).menuItemId(2L).quantity(3).build()
//        );
//        List<OrderLine> secondOrderLines = List.of(
//                OrderLine.builder().id(3L).menuItemId(3L).menuItemName("Pizza").price(8.0).quantity(1).build(),
//                OrderLine.builder().id(4L).menuItemId(4L).menuItemName("Soda").price(2.0).quantity(2).build()
//        );
//        List<Order> orders = List.of(
//                Order.builder().id(1L).restaurantId(1L).userId(1L).deliveryAddress("123 Main St").orderLines(firstOrderLines).totalPrice(10.0).build(),
//                Order.builder().id(2L).restaurantId(2L).userId(2L).deliveryAddress("456 Elm St").orderLines(secondOrderLines).totalPrice(20.0).build()
//        );
//        when(orderRepository.findAll()).thenReturn(orders);
//
//        List<OrderResponseDto> result = orderService.getAllOrders();
//
//        assertEquals(2, result.size());
//        assertEquals(1L, result.get(0).getOrderId());
//        assertEquals(2L, result.get(1).getOrderId());
//        assertEquals(2, result.get(0).getOrderLines().size());
//        assertEquals(2, result.get(1).getOrderLines().size());
//        verify(orderRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getAllOrdersWhenNoOrdersAvailable() {
//        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<OrderResponseDto> result = orderService.getAllOrders();
//
//        assertTrue(result.isEmpty());
//        verify(orderRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getOrderByIdSuccessfully() {
//        Order order = Order.builder()
//                .id(1L)
//                .restaurantId(1L)
//                .userId(1L)
//                .deliveryAddress("123 Main St")
//                .orderLines(List.of(
//                        OrderLine.builder().id(1L).menuItemId(1L).menuItemName("Burger").price(5.0).quantity(2).build(),
//                        OrderLine.builder().id(2L).menuItemId(2L).menuItemName("Fries").price(3.0).quantity(1).build()
//                ))
//                .totalPrice(13.0)
//                .build();
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        OrderResponseDto result = orderService.getOrderById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getOrderId());
//        assertEquals("123 Main St", result.getDeliveryAddress());
//        assertEquals(2, result.getOrderLines().size());
//        verify(orderRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void getOrderByIdNotFound() {
//        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
//        verify(orderRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void updateOrderStatusSuccessfully() {
//        Order order = Order.builder()
//            .id(1L)
//                .status(OrderStatus.CREATED)
//                .build();
//    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//    orderService.updateOrderStatus(1L, OrderStatus.DELIVERED);
//
//    verify(orderRepository, times(1)).save(order);
//    assertEquals(OrderStatus.DELIVERED, order.getStatus());
//}
//
//@Test
//void updateOrderStatusNotFound() {
//    when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//
//    assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.DELIVERED));
//}
//
//}
