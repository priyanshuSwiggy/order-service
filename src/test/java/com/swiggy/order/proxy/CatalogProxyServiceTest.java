package com.swiggy.order.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.order.dto.MenuItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatalogProxyServiceTest {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    private CatalogProxyService catalogProxyService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        catalogProxyService = new CatalogProxyService(restTemplate, objectMapper);
    }

    @Test
    void getMenuItemByIdAndRestaurantIdSuccessfully() throws Exception {
        String response = "{\"id\":1,\"name\":\"Burger\",\"price\":5.0}";
        MenuItemDto menuItemDto = MenuItemDto.builder().id(1L).name("Burger").price(5.0).build();

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(response);
        when(objectMapper.readValue(response, MenuItemDto.class)).thenReturn(menuItemDto);

        MenuItemDto result = catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Burger", result.getName());
        assertEquals(5.0, result.getPrice());
    }

    @Test
    void getMenuItemByIdAndRestaurantIdThrowsExceptionOnInvalidResponse() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("invalid response");
        when(objectMapper.readValue(anyString(), eq(MenuItemDto.class))).thenThrow(new RuntimeException("Failed to parse"));

        assertThrows(RuntimeException.class, () -> catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L));
    }

    @Test
    void getMenuItemByIdAndRestaurantIdThrowsExceptionOnRestTemplateError() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("RestTemplate error"));

       assertThrows(RuntimeException.class, () -> catalogProxyService.getMenuItemByIdAndRestaurantId(1L, 1L));
    }
}