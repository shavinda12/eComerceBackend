package com.ecommercebackend.store.payments;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("Payment not found");
    }
}
