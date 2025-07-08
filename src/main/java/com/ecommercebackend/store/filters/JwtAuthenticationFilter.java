package com.ecommercebackend.store.filters;

import com.ecommercebackend.store.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    //this is a jwt validation filter so this is acting as a middleware

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         var authHeader=request.getHeader("Authorization");

         if(authHeader == null || !authHeader.startsWith("Bearer ")) {
             //in here if there is no authorization header this will give the request to nest filter chain
             //so request like login they don't have a jwt token
             filterChain.doFilter(request, response);
             return;
         }
         var token=authHeader.replace("Bearer ", "");

         if(!jwtService.validateToken(token)){
             filterChain.doFilter(request, response);
             return;
         }

         //this is storing the details about the current user using security context holder
        //so in the security context handler we can pass those values
        //so first argument is the pricncipal that should be a id or something
        //second one should be credential
        //third one should be the role of the user

        var role=jwtService.getRoleFromToken(token);
         var userId=jwtService.getUserIdFromToken(token);
         var authentication=new UsernamePasswordAuthenticationToken(
                 userId
                 ,null,
                  List.of(new SimpleGrantedAuthority("ROLE_"+role.name())));
         authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
         SecurityContextHolder.getContext().setAuthentication(authentication);

         filterChain.doFilter(request, response);

    }
}
