package com.swiggy.order.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.order.dto.MenuItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogProxyService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MenuItemDto getMenuItemByIdAndRestaurantId(Long restaurantId, Long menuItemId) {
        String url = "http://localhost:8080/catalog/restaurants/" + restaurantId + "/menuItems/" + menuItemId;
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("Raw response from catalog service: {}", response);
            return objectMapper.readValue(response, MenuItemDto.class);
        } catch (HttpClientErrorException e) {
            log.error("Error response from catalog service: {}", e.getResponseBodyAsString());
            throw new RuntimeException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Failed to parse menu item response: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
