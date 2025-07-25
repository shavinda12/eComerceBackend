package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.JwtResponse;
import com.ecommercebackend.store.dtos.LoginRequestDto;
import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.exceptions.UserEmailNotFoundException;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.UserRepository;
import com.ecommercebackend.store.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<JwtResponse> login(@Valid  @RequestBody LoginRequestDto request, HttpServletResponse response){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken=jwtService.generateAccessToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        var cookie=new Cookie("refreshToken",refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(604800); //7 days
        cookie.setSecure(true);
        response.addCookie(cookie);


        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@CookieValue(name = "refreshToken") String refreshToken){
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt==null || jwt.isExpired(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId=jwt.getUserIdFromToken(refreshToken);
        var user=userRepository.findById(userId).orElseThrow(null);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var newAccessToken=jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long) authentication.getPrincipal();

        var user=userRepository.findById(userId).orElseThrow(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Void> handleBadCredentials(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({UserEmailNotFoundException.class})
    public ResponseEntity<Void> handleUserEmailNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
