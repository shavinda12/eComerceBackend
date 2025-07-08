package com.ecommercebackend.store.config;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    private String secret;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    public SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

}
