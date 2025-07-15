package com.ecommercebackend.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private int quantity;
    private BigDecimal total_price;

}
