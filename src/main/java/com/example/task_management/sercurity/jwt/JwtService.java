package com.example.task_management.sercurity.jwt;

import com.example.task_management.common.cache.RedisService;
import com.example.task_management.common.exception.InvalidTokenException;
import com.example.task_management.sercurity.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey signingKey;

    private final RedisService redisService;


    private synchronized SecretKey getSigningKey() {
        if (signingKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return signingKey;
    }

    // GENERATE
    public String generateAccessToken(CustomUserDetails userDetails) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = Map.of(
                "authorities", authorities,
                "uid", userDetails.getUserId().toString(),
                "type", "ACCESS"
        );

        return buildToken(userDetails.getUsername(), accessTokenExpiration, claims);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        // Refresh token dont need Authorities
        return buildToken(userDetails.getUsername(), refreshTokenExpiration, new HashMap<>());
    }

    public String generatePasswordResetToken(String email) {
        long resetPasswordTokenExpiration = 15 * 60_000L; //60000 mili sec = 60 sec = 1 mins
        return buildToken(email, resetPasswordTokenExpiration, Map.of("type", "PASSWORD_RESET"));
    }

    private String buildToken(String subject, long expirationMs, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plus(expirationMs, ChronoUnit.MILLIS);

        return Jwts.builder()
                .claims(extraClaims)
                .claim("sub", subject)
                .claim("iat", now.getEpochSecond())
                .claim("exp", exp.getEpochSecond())
                .signWith(getSigningKey())
                .compact();
    }

    // EXTRACT CLAIMS
    private Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid Token: " + e.getMessage());
        }
    }

    public String extractEmail(String token) {
        return parse(token).getSubject();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parse(token).get("uid", String.class));
    }

    public String extractType(String token) {
        return parse(token).get("type", String.class);
    }

    public List<String> extractAuthorities(String token) {
        return parse(token).get("authorities", List.class);
    }

    public Instant extractExpiration(String token) {
        return parse(token).getExpiration().toInstant();
    }

    // VALIDATION

    //check access token is valid
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            String type = extractType(token);

            return email.equals(userDetails.getUsername())
                    && "ACCESS".equals(type)
                    && !isTokenExpired(token);
        } catch (InvalidTokenException e) {
            log.debug("Access token validation failed: {}", e.getMessage());
            return false;
        }
    }

    //check refresh token is valid
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            String type = extractType(token);

            return email.equals(userDetails.getUsername())
                    && "REFRESH".equals(type)
                    && !isTokenExpired(token);
        } catch (InvalidTokenException e) {
            log.debug("Refresh token validation failed: {}", e.getMessage());
            return false;
        }
    }

    //check  token is expired
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).isBefore(Instant.now());
        } catch (InvalidTokenException e) {
            return true;
        }
    }

    // helper

    // get Remaining Expiration time token
    public long getRemainingSeconds(String token) {
        try {
            Instant expiration = extractExpiration(token);
            return Math.max(0, java.time.Duration.between(Instant.now(), expiration).getSeconds());
        } catch (io.jsonwebtoken.JwtException e) {
            log.debug("Cannot calculate TTL for token due to JWT Exception: {}", e.getMessage());
            return 0;
        } catch (Exception e) {

            log.error("Unexpected error when calculating TTL for token", e);
            return 0;
        }
    }

    // get email token reset password
    public String validatePasswordResetToken(String token) {
        Claims claims = parse(token);

        if (!"PASSWORD_RESET".equals(claims.get("type"))) {
            throw new InvalidTokenException("Invalid Token for Password Reset");
        }
        return claims.getSubject();
    }

    // check email in token
    public boolean isTokenValidForSubject(String token, String expectedEmail) {
        try {
            return expectedEmail.equals(extractEmail(token))
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


}
