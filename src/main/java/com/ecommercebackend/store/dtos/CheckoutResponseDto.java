package com.ecommercebackend.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResponseDto {
    private Long orderId;
}
