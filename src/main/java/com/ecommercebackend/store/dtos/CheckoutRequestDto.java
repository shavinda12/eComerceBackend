package com.ecommercebackend.store.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    private UUID cartId;
}
