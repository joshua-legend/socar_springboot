package com.example.socar.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "koreaITJoshuakoreaITJoshuakoreaITJoshuakoreaITJoshua"
            ));

    private final long expirationTime = 1000 * 10;

    // JWT 생성
    public static String generateToken(Authentication auth) {
        User user = (User) auth.getPrincipal();
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("username", user.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 10))
                .signWith(key)
                .compact();
    }

    // JWT에서 클레임 추출
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractToken(token).getSubject();
    }

    // JWT 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractToken(token).getExpiration().before(new Date());
    }

    // JWT 검증
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
