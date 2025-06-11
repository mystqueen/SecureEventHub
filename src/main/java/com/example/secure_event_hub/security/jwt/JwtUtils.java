package com.example.secure_event_hub.security.jwt;

import com.example.secure_event_hub.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final ObjectMapper objectMapper;
    @Value("${prox.app.jwtSecret}")
    private String jwtSecret;
    @Value("${prox.app.jwtExpirationMs}")
    private String jwtExpirationMs;
    @Value("${prox.app.jwtRefreshExpirationMs}")
    private String jwtRefreshExpirationMs;

    public JwtUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractEmailFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, jwtRefreshExpirationMs);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String email = extractEmailFromJwtToken(token);
        return (email.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).toInstant().isBefore(Instant.now());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpirationMs);
    }

    public String generateTokenWithUserDetails(User user) {
        @SuppressWarnings("unchecked")
        Map<String, Object> extraClaims = objectMapper.convertValue(user, Map.class);
        extraClaims.remove("password");
        return buildToken(extraClaims, user, jwtExpirationMs);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, String jwtExpire) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(Long.parseLong(jwtExpire));
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSignInKey())
                .compact();
    }
}
