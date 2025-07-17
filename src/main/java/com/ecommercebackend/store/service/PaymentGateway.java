package com.ecommercebackend.store.service;

import com.ecommercebackend.store.entities.Order;
import com.stripe.param.checkout.SessionCreateParams;

public interface PaymentGateway {
    CheckOutSession createSession(Order order);
}
