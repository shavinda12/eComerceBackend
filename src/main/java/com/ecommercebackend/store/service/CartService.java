package com.ecommercebackend.store.service;

import com.ecommercebackend.store.dtos.AddItemToCartRequest;
import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.dtos.CartItemDto;
import com.ecommercebackend.store.dtos.UpdateCartItemDto;
import com.ecommercebackend.store.entities.Cart;
import com.ecommercebackend.store.exceptions.CartNotFoundException;
import com.ecommercebackend.store.exceptions.ProductNotFoundException;
import com.ecommercebackend.store.mappers.CartMapper;
import com.ecommercebackend.store.repositories.CartRepository;
import com.ecommercebackend.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartMapper cartMapper;

    public CartDto createCart() {
        var cart=new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, AddItemToCartRequest request){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        var product=productRepository.findById(request.getProductId()).orElse(null);
        if(product==null){
            throw new ProductNotFoundException();
        }
        var cartItem=cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCartById(UUID cartId){
        var cart=cartRepository.getCartWithItems( cartId).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, UpdateCartItemDto request){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }

        var cartItem=cart.getCartItem(productId);
        if(cartItem==null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        var product=cart.getCartItem(productId);
        if(product==null){
            throw new ProductNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart=cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        cart.clearItems();
        cartRepository.save(cart);
    }
}
