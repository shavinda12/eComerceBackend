package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.AddItemToCartRequest;
import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.dtos.CartItemDto;
import com.ecommercebackend.store.dtos.UpdateCartItemDto;
import com.ecommercebackend.store.exceptions.CartItemNotFoundException;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.exceptions.ProductNotFoundException;
import com.ecommercebackend.store.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
        var cartDto=cartService.createCart();
        return ResponseEntity.created(builder.path("/cart/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto );
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(@PathVariable(name = "cartId") UUID cartId, @RequestBody  AddItemToCartRequest request){
        CartItemDto cartItemDto=cartService.addToCart(cartId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCartById(@PathVariable(name="cartId") UUID cartId){
        return cartService.getCartById(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCardItem(@PathVariable(name = "cartId") UUID cartId, @PathVariable(name = "productId") Long productId,@Valid @RequestBody UpdateCartItemDto request){
        return cartService.updateCartItem(cartId,productId,request);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable(name = "cartId") UUID cartId, @PathVariable(name = "productId") Long productId){
        cartService.deleteCartItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "cartId") UUID cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","product not found in the cart"));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartItemNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart item not found"));
    }
}
