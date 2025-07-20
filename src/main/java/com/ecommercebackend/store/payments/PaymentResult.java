package com.ecommercebackend.store.payments;

import com.ecommercebackend.store.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult  {
    private Long orderId;
    private OrderStatus  paymentStatus;
}
