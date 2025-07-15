package com.ecommercebackend.store.service;

import com.ecommercebackend.store.dtos.CheckoutRequestDto;
import com.ecommercebackend.store.dtos.CheckoutResponseDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.exceptions.CartEmptyException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public CheckoutResponseDto checkout(CheckoutRequestDto request){
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
        cartService.clearCart(request.getCartId());
        return new CheckoutResponseDto(order.getId());
    }

}
