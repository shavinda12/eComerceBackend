package com.ecommercebackend.store.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    public String generateToken(String email){
        final long tokenExpiration=86400; //1 day in seconds
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
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
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

}
