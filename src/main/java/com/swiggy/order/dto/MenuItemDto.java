package com.swiggy.order.dto;

import com.swiggy.order.enums.Currency;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class MenuItemDto {
    private Long id;
    private String name;
    private double price;
    private Currency currency;
}
