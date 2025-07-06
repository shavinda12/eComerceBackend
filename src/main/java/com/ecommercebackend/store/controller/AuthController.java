package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.JwtResponse;
import com.ecommercebackend.store.dtos.LoginRequestDto;
import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.UserRepository;
import com.ecommercebackend.store.service.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid  @RequestBody LoginRequestDto request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var jwt=jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/validate")
    public boolean validateToken (@RequestHeader("Authorization") String authHeader){
        System.out.println("Validation token called");
        var token=authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(){
        var authenticataion=SecurityContextHolder.getContext().getAuthentication();
        var email=(String) authenticataion.getPrincipal();

        var user=userRepository.findByEmail(email).orElseThrow(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Void> handleBadCredentials(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
