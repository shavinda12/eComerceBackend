package com.ecommercebackend.store.service;

import com.ecommercebackend.store.config.JwtConfig;
import com.ecommercebackend.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Data
public class Jwt {
    private final Claims claims;
    private final SecretKey secret;

    public Jwt(Claims claims,SecretKey secret){
        this.claims=claims;
        this.secret=secret;
    }


    public Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(this.secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean isExpired(String token) {
        return this.claims.getExpiration().before(new Date());
    }

    public Long getUserIdFromToken(String token){
        return Long.valueOf(this.claims.getSubject());
    }

    public Role getRoleFromToken(String token){
        return Role.valueOf(this.claims.get("role",String.class));
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secret).compact();
    }




}
