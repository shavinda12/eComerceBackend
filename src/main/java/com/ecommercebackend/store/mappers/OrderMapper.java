package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.OrderDto;
import com.ecommercebackend.store.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "created_at",source = "created_at")
    OrderDto toDto(Order order);
}
