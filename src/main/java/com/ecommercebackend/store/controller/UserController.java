package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getUsers(){
       return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
       var user=userRepository.findById(id).orElse(null);
        if(user==null){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(userMapper.toDto(user));
    }
}
