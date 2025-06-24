package com.ecommercebackend.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Long categoryId;
}
