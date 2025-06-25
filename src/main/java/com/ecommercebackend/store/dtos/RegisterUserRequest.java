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

    @Min(value = 6,message = "Password should be more than 6 characters")
    @Max(value= 25,message="Password should be below 25 characters")
    @NotNull(message = "Password should be required")
    private String password;
}
