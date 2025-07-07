package com.ecommercebackend.store.config;

import com.ecommercebackend.store.filters.JwtAuthenticationFilter;
import com.ecommercebackend.store.service.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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

        http
                .sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(c -> c.disable())
                .authorizeHttpRequests(c->
                        c.requestMatchers("/carts/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //so in here if the authentication is failded like expired access token this security filter will issue 403 forbidden by default
                //but it is not the good way.the status code should be unauthorized
                //so this will override the status code of the expired access token issue as unauthorized
                .exceptionHandling(
                        c->
                                c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        return http.build();
    }

    @Bean
    //This is authentication provide which is inbuilt authentication for spring.
    public AuthenticationProvider authenticationProvider() {
         var provider= new DaoAuthenticationProvider();
         provider.setPasswordEncoder(passwordEncoder());
         provider.setUserDetailsService(userDetailsService);
         return provider;
    }

    @Bean
    //registering autthenication provide bean on authenticactionManager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
