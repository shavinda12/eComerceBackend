package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.LoginRequestDto;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid  @RequestBody LoginRequestDto request){
        var user=userRepository.findByEmail(request.getEmail()).orElse(null);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
            return ResponseEntity.ok(userMapper.toDto(user));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
