package com.ecommercebackend.store.service;

import com.ecommercebackend.store.config.JwtConfig;
import com.ecommercebackend.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;
    public String generateAccessToken(User user){
        //1 day in seconds
        return generateToken(user,jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user){
        return generateToken(user,jwtConfig.getRefreshTokenExpiration());
    }

    public String generateToken(User user, long tokenExpiration){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("name",user.getName())
                .claim("role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*tokenExpiration))
                .signWith(jwtConfig.generateSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaimsFromToken(token);
            return claims.getExpiration().after(new Date());//this will check the expiration
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(jwtConfig.generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token){
        return Long.valueOf(getClaimsFromToken(token).getSubject());
    }




}
