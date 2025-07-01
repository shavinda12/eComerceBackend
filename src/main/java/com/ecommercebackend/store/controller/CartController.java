package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.entities.Cart;
import com.ecommercebackend.store.mappers.CartMapper;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
        var cart=new Cart();
        cartRepository.save(cart);
        CartDto cartDto=cartMapper.toDto(cart);
        return ResponseEntity.created(builder.path("/cart/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto );
    }
}
