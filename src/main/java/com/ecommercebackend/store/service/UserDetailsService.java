package com.ecommercebackend.store.service;

import com.ecommercebackend.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         var user=userRepository.findByEmail(email).orElseThrow(
                 ()->new UsernameNotFoundException("User not found"));
         return new User(user.getEmail(),user.getPassword(), Collections.emptyList());
    }
}