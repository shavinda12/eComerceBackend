package com.ecommercebackend.store.service;

import com.ecommercebackend.store.entities.User;
import com.ecommercebackend.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElseThrow(null);
    }
}
