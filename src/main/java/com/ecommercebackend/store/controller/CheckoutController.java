package com.ecommercebackend.store.controller;


import com.ecommercebackend.store.dtos.CheckoutRequestDto;
import com.ecommercebackend.store.dtos.CheckoutResponseDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.entities.OrderItem;
import com.ecommercebackend.store.entities.OrderStatus;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.ecommercebackend.store.service.AuthService;
import com.ecommercebackend.store.service.CartService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody  CheckoutRequestDto request){
        var cart=cartRepository.findById(request.getCartId()).orElse(null);
        if(cart==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","cart not found"));
        }
        if(cart.getItems().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","cart is empty"));
        }
        var order=Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);
        cartService.clearCart(request.getCartId());
        return ResponseEntity.status(HttpStatus.OK).body(new CheckoutResponseDto(order.getId()));
    }


}
