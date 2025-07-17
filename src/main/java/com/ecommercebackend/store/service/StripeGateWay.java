package com.ecommercebackend.store.service;

import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.exceptions.PaymentNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeGateWay implements PaymentGateway {

    @Value("${webSiteUrl}")
    private String webSiteUrl;

    @Override
    public CheckOutSession createSession(Order order) {
        try{
            var builder= SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl+"/checkout-success?orderId="+order.getId() )
                    .setCancelUrl(webSiteUrl+"/checkout-cancel");

            order.getItems().forEach(item->{
                var lineItem=SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(item.getUnit_price().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build( )
                                        ).build()
                        ).build();
                builder.addLineItem(lineItem);
            });
            var session= Session.create(builder.build());
            return new CheckOutSession(session.getUrl());
        }catch(StripeException e){
            throw new PaymentNotFoundException();
        }

    }
}
