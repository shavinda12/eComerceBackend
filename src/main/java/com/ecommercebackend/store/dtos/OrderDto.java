package com.ecommercebackend.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime created_at;
    private List<OrderItemDto> items;
    private BigDecimal total_price;
}
