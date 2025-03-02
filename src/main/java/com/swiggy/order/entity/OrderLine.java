package com.swiggy.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swiggy.order.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_lines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuItemId;
    private String menuItemName;
    private double price;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
