package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.AddItemToCartRequest;
import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.dtos.UpdateCartItemDto;
import com.ecommercebackend.store.entities.Cart;
import com.ecommercebackend.store.entities.CartItem;
import com.ecommercebackend.store.mappers.CartMapper;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.ProductRepository;
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
        var cartItem= cart.getItems().stream().filter(item->item.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
         if(cartItem!=null){
             cartItem.setQuantity(cartItem.getQuantity()+1);
         }
         else{
             cartItem=new CartItem();
             cartItem.setCart(cart);
             cartItem.setProduct(product);
             cartItem.setQuantity(1);
             cart.getItems().add(cartItem);
         }
        cartRepository.save(cart);
        var cartItemDto=cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable(name="cartId") UUID cartId){
        var cart=cartRepository.getCartWithItems( cartId).orElse(null);
        if(cart==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCardItem(@PathVariable(name = "cartId") UUID cartId, @PathVariable(name = "productId") Long productId,@Valid @RequestBody UpdateCartItemDto request){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart not found"));
        }

        var cartItem=cart.getItems().stream().filter(item->item.getProduct().getId().equals(productId)).findFirst().orElse(null);
        if(cartItem==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","product not found"));
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }
}
