package com.ecommercebackend.store.payments;

import com.ecommercebackend.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckOutSession createSession(Order order);
    Optional <PaymentResult> parseWebHookEvent(WebhookRequest request);

}
