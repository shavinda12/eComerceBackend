package com.ecommercebackend.store.service;

import com.ecommercebackend.store.config.JwtConfig;
import com.ecommercebackend.store.entities.Role;
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
    public Jwt generateAccessToken(User user){
        //1 day in seconds
        return generateToken(user,jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user){
        return generateToken(user,jwtConfig.getRefreshTokenExpiration());
    }

    public Jwt generateToken(User user, long tokenExpiration){
        var claims=Jwts.claims()
                .subject(user.getId().toString())
                .add("email",user.getEmail())
                .add("name",user.getName())
                .add("role",user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*tokenExpiration))
                .build();
        return new Jwt(claims,jwtConfig.generateSecretKey());
    }

    public Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith(this.jwtConfig.generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public Jwt parseToken(String token){
        try{
            var claims=getClaimsFromToken(token);
            return new Jwt(claims,jwtConfig.generateSecretKey());
        }catch(JwtException e){
            return null;
        }

    }






}
