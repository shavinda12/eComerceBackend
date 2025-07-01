package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.AddItemToCartRequest;
import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.entities.Cart;
import com.ecommercebackend.store.entities.CartItem;
import com.ecommercebackend.store.mappers.CartMapper;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.CategoryRepository;
import com.ecommercebackend.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
        var cart=new Cart();
        cartRepository.save(cart);
        CartDto cartDto=cartMapper.toDto(cart);
        return ResponseEntity.created(builder.path("/cart/{id}").buildAndExpand(cartDto.getId()).toUri()).body(cartDto );
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(@PathVariable(name = "cartId") UUID cartId, @RequestBody  AddItemToCartRequest request){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            return ResponseEntity.notFound().build();
        }
        var product=productRepository.findById(request.getProductId()).orElse(null);
        if(product==null){
            return ResponseEntity.badRequest().build();
        }
        var cartItem= cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
         if(cartItem!=null){
             cartItem.setQuantity(cartItem.getQuantity()+1);
         }
         else{
             cartItem=new CartItem();
             cartItem.setCart(cart);
             cartItem.setProduct(product);
             cartItem.setQuantity(1);
             cart.getCartItems().add(cartItem);
         }
        cartRepository.save(cart);
        var cartItemDto=cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
}
