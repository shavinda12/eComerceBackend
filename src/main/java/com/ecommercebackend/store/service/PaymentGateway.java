package com.ecommercebackend.store.service;

import com.ecommercebackend.store.entities.Order;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.Optional;

public interface PaymentGateway {
    CheckOutSession  createSession(Order order);
    Optional <PaymentResult> parseWebHookEvent(WebhookRequest request);

}
