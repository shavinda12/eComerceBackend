package com.ecommercebackend.store.filters;

import com.ecommercebackend.store.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    //this is a jwt validation filter so this is acting as a middleware

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         var authHeader=request.getHeader("Authorization");

         if(authHeader == null && !authHeader.startsWith("Bearer ")) {
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

         var authentication=new UsernamePasswordAuthenticationToken(jwtService.getEmailFromToken(token),null,null);
         authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
         SecurityContextHolder.getContext().setAuthentication(authentication);

         filterChain.doFilter(request, response);

    }
}
