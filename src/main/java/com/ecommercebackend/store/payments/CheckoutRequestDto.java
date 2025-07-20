package com.ecommercebackend.store.payments;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    private UUID cartId;
}
