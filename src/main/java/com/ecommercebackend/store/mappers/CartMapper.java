package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.dtos.CartItemDto;
import com.ecommercebackend.store.entities.Cart;
import com.ecommercebackend.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target="totalPrice",expression="java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target="totalPrice",expression="java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
