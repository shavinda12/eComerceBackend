package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target="createdAt" ,expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
}
