package com.ecommercebackend.store.config;

import com.ecommercebackend.store.entities.Role;
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
                        c.requestMatchers(HttpMethod.POST,"/users/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.GET,"/admin/**").hasRole(Role.ADMIN.name())
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        c->{
                            //so in here if the authentication is failded like expired access token this security filter will issue 403 forbidden by default
                            //but it is not the good way.the status code should be unauthorized
                            //so this will override the status code of the expired access token issue as unauthorized
                            c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

                            //This is the second error handling which is saying if the user is authenticated
                            //If he doesn't have the correct role then the status code should be not authorized
                            //it should be forbidden 403
                            //so we have to override the above status
                            c.accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                            });
                        }

                );

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
