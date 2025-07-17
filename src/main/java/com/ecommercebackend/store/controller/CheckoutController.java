package com.ecommercebackend.store.controller;


import com.ecommercebackend.store.dtos.CheckoutRequestDto;
import com.ecommercebackend.store.dtos.CheckoutResponseDto;
import com.ecommercebackend.store.dtos.ErrorDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.entities.OrderItem;
import com.ecommercebackend.store.entities.OrderStatus;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.ecommercebackend.store.service.AuthService;
import com.ecommercebackend.store.service.CartService;
import com.ecommercebackend.store.service.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;


    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody  CheckoutRequestDto request){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(checkoutService.checkout(request));
        }
        catch(StripeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR). body(new ErrorDto("Something went wrong in stripe"));
        }

    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCheckoutException(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }


}
