package com.ecommercebackend.store.payments;

import lombok.Data;

@Data
public class CheckoutResponseDto {
    private Long orderId;
    private String checkOutUrl;

    public CheckoutResponseDto(Long orderId, String checkOutUrl) {
        this.orderId = orderId;
        this.checkOutUrl = checkOutUrl;
    }
}
