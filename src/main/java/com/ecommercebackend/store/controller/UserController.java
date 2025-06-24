package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.RegisterUserRequest;
import com.ecommercebackend.store.dtos.UserDto;
import com.ecommercebackend.store.mappers.UserMapper;
import com.ecommercebackend.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getUsers(@RequestParam(required = false,defaultValue = "",name = "sort")  String sort){
       if(!Set.of("name","email").contains(sort)){
           sort="name";
       }
       return userRepository.findAll(Sort.by(sort)).stream().map(userMapper::toDto).toList();

    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
       var user=userRepository.findById(id).orElse(null);
        if(user==null){
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public  ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder){
        var user=userMapper.toEntity(request);
        userRepository.save(user);
        var userDto=userMapper.toDto(user);
        var uri=uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);

        //in here for user creation we used  status created which is 201
        //so this created status wants the location of the source in which created
        //so we get it by using uri,uriBuilder
        //if the created source only wants to share between internal services we don't need created.just 200 is okay
        //otherwise it is good to embedded the location in the response

    }
}
