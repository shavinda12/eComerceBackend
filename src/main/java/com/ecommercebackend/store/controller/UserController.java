package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.entities.User;
import com.ecommercebackend.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;

    @GetMapping("/user")
    public Iterable<User> getUsers(){
       return  userRepository.findAll();
    }
}
