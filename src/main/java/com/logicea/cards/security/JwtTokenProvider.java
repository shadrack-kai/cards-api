package com.logicea.cards.security;

import com.logicea.cards.config.ConfigProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private final ConfigProperties properties;

    public String generateToken(final Authentication authentication, final Long userId) {
        final User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setClaims(Map.of("role", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), "id", userId))
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (properties.getValidityPeriod() * 1000L)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        return (Integer) getClaimsFromJwtToken(token).get("id");
    }

    public List<String> getRolesFromToken(String token) {
        return (List<String>) getClaimsFromJwtToken(token).get("role");
    }

    public String getEmailFromJwtToken(String token) {
        return getClaimsFromJwtToken(token).getSubject();
    }

    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parse(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT token is unsupported: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
    }

}