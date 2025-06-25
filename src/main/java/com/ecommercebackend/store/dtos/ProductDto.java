package com.ecommercebackend.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProductDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Long categoryId;
}
