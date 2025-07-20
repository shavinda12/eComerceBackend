package com.ecommercebackend.store.controller;


import com.ecommercebackend.store.dtos.CheckoutRequestDto;
import com.ecommercebackend.store.dtos.CheckoutResponseDto;
import com.ecommercebackend.store.dtos.ErrorDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.entities.OrderItem;
import com.ecommercebackend.store.entities.OrderStatus;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.exceptions.PaymentNotFoundException;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.ecommercebackend.store.service.AuthService;
import com.ecommercebackend.store.service.CartService;
import com.ecommercebackend.store.service.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;


    @Value("${stripe.webHookSecretKey}")
    private String webHookSecretKey;



    //So this will conert our cart object in to a order once user wants to purchase

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody  CheckoutRequestDto request){
            return ResponseEntity.status(HttpStatus.OK).body(checkoutService.checkout(request));

    }


    //When Stripe payment is done this request is comming from strip to server
    //This will tell this is a succeess payment or failed payment
    //So then server will update the exact payment status as failed or success
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebHook(@RequestHeader("Stripe-Signature") String signature,@RequestBody  String payload){
        try {
            var event= Webhook.constructEvent(payload,signature,webHookSecretKey);
            System.out.println(event);

            var stripeObject= event.getDataObjectDeserializer().getObject().orElse(null);

            switch(event.getType()){
                case "payment_intent.succeeded"->{
                    var paymentIntent=(PaymentIntent) stripeObject;
                    if(paymentIntent!=null){
                        var order_id= paymentIntent.getMetadata().get("order_id");
                        var order=orderRepository.findById(Long.valueOf( order_id)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.failed"->{
                    //update as failed
                }
            }
            return ResponseEntity.ok().build();


        } catch (SignatureVerificationException e) {
             return ResponseEntity.badRequest().build();
        }

    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<?> paymentNotFound(PaymentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Payment not found"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCheckoutException(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }


}
