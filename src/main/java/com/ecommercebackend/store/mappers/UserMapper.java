package com.ecommercebackend.store.mappers;

import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.entities.User;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
