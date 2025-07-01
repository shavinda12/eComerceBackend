package com.ecommercebackend.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @NotNull(message = "productId is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Max(value = 1000, message = "Quantity must be less than 100")
    private Integer quantity;
}
