package com.ecommercebackend.store.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class RegisterUserRequest {
    @NotNull(message = "name is required")
    @Size(max = 255,message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 6,max = 25,message = "Password should be between 6 and 25 characters")
    @NotNull(message = "Password should be required")
    private String password;
}
