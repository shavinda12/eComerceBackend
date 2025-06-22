package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dto.UserDto;
import com.ecommercebackend.store.entities.User;
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

    @GetMapping
    public Iterable<UserDto> getUsers(){
       return userRepository.findAll().stream().map(user->new UserDto(user.getName(),user.getEmail(),user.getId())).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
       var user=userRepository.findById(id).orElse(null);
       if(user==null){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(new UserDto(user.getName(),user.getEmail(),user.getId()));
    }
}
