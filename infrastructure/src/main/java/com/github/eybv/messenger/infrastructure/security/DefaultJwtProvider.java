package com.github.eybv.messenger.infrastructure.security;

import com.github.eybv.messenger.application.security.JwtProvider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class DefaultJwtProvider implements JwtProvider {

    @Value("${security.jwt-secret}")
    private String secret;

    @Value("${security.jwt-expiration}")
    private long expiration;

    @Override
    public String createToken(String subject, Map<String, String> claims) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(subject);
        builder.setExpiration(getExpiryTimeFromNow());
        builder.signWith(Keys.hmacShaKeyFor(keyBytes));
        claims.forEach(builder::claim);

        return builder.compact();
    }

    @Override
    public Optional<String> getSubjectFromToken(String token) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        try {
            JwtParserBuilder builder = Jwts.parserBuilder();
            builder.setSigningKey(Keys.hmacShaKeyFor(keyBytes));
            Jws<Claims> claims = builder.build().parseClaimsJws(token);

            return Optional.ofNullable(claims.getBody().getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Date getExpiryTimeFromNow() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
