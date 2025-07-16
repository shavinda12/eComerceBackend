package com.ecommercebackend.store.service;

import com.ecommercebackend.store.dtos.CheckoutRequestDto;
import com.ecommercebackend.store.dtos.CheckoutResponseDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Value("${webSiteUrl}")
    private String webSiteUrl;

    public CheckoutResponseDto checkout(CheckoutRequestDto request) throws StripeException {
        var cart=cartRepository.findById(request.getCartId()).orElse(null);
        if(cart==null){
            System.out.println("error is in checkout service");
            throw new CartNotFoundException();
        }
        if(cart.getItems().isEmpty()){
            throw new CartEmptyException();
        }
        var order= Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);
        //create a checkout session
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
                                    .setUnitAmountDecimal(item.getUnit_price())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getProduct().getName())
                                                    .build( )
                                    ).build()
                    ).build();
            builder.addLineItem(lineItem);
        });
        var session= Session.create(builder.build());
        cartService.clearCart(request.getCartId());
        return new CheckoutResponseDto(order.getId(),session.getUrl());

    }

}
