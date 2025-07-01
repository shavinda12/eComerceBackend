package com.ecommercebackend.store.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "productId is required")
    private Long productId;
}
