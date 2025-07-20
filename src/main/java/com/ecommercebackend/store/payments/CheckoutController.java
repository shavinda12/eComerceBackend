package com.ecommercebackend.store.payments;


import com.ecommercebackend.store.dtos.ErrorDto;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;






    //So this will conert our cart object in to a order once user wants to purchase

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody  CheckoutRequestDto request){
            return ResponseEntity.status(HttpStatus.OK).body(checkoutService.checkout(request));

    }


    //When Stripe payment is done this request is comming from strip to server
    //This will tell this is a succeess payment or failed payment
    //So then server will update the exact payment status as failed or success
    @PostMapping("/webhook")
    public void  handleWebHook(@RequestHeader   Map<String,String> headers ,@RequestBody  String payload){
        checkoutService.handleWebHookEvent(new WebhookRequest(headers,payload));
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
