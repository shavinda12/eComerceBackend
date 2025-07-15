package com.ecommercebackend.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "name is required")
    @Email
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
