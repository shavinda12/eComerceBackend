package com.ecommercebackend.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    //This is a password encoder in springboot
    //so Password encoder is a interface and BcryptPasswordEncoder is a class which implement it.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //in here we do mainly three configuration
        //1 stateless session is used (no cookies,just tokens)
        //2 disable csrf -> csrf is attack where browser get tricked in to making request on behalf of a user
        //csrf attacks security implemented for traditional web sites.fo performance issues we disable these.
        //Authorizing routes describe which end points are are public and protected

        http .sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(c -> c.disable())
                .authorizeHttpRequests(c->
                        c.requestMatchers("/carts/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                                .anyRequest().authenticated());


        return http.build();
    }
}
