package com.example.moviespot.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${app.jwtExpirationInMs:900000}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        return Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(now).setExpiration(new Date(now.getTime() + jwtExpirationInMs))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512).compact();
    }

    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        return Jwts.builder().setSubject(username)
                .setIssuedAt(now).setExpiration(new Date(now.getTime() + jwtExpirationInMs))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512).compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try { Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token); return true; }
        catch (Exception e) { System.out.println("Invalid JWT: " + e.getMessage()); }
        return false;
    }
}
