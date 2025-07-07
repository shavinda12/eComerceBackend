package com.ecommercebackend.store.service;

import com.ecommercebackend.store.entities.User;
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
    public String generateAccessToken(User user){
        final long accessTokenExpiration=86400; //1 day in seconds
        return generateToken(user,accessTokenExpiration);
    }

    public String generateRefreshToken(User user){
        final long refreshTokenExpiration=604800;
        return generateToken(user,refreshTokenExpiration);
    }

    public String generateToken(User user, long tokenExpiration){
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("name",user.getName())
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

    public Long getUserIdFromToken(String token){
        return Long.valueOf(getClaimsFromToken(token).getSubject());
    }




}
