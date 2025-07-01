package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.CartDto;
import com.ecommercebackend.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
