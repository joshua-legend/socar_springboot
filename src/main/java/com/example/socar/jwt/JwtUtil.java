package com.example.socar.jwt;

import com.example.socar.admin.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


    private static final String SECRET_KEY = "UmmIsStillAliveUmmIsStillAliveUmmIsStillAliveUmmIsStillAliveUmmIsStillAliveUmmIsStillAliveUmmIsStillAlive";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // JWT 생성
    public static String generateToken(Admin admin) {
        return Jwts.builder()
                .claim("username", admin.getAdminId())
                // 사용자 ID를 주체로 설정
                .subject(admin.getAdminId())
                // 토큰이 발급된 시간을 현재 시간으로 설정
                .issuedAt(new Date(System.currentTimeMillis()))
                // 토큰의 만료 시간을 현재 시간으로부터 10초 후로 설정
                .expiration(new Date(System.currentTimeMillis() + 1000 * 10))
                // 서명 알고리즘과 비밀 키를 사용하여 토큰에 서명
                .signWith(key)
                // JWT 토큰을 생성하고 문자열로 반환
                .compact();
    }


    // JWT에서 클레임 추출
    public static Claims extractToken(String token) {
        // 주어진 토큰을 파싱하여 서명을 검증하고 클레임 정보를 추출
        Claims claims = Jwts.parser()
                // 서명을 검증할 때 사용할 키 설정
                .verifyWith(key)
                .build()
                // 토큰을 파싱하고 클레임 정보를 가져옴
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    // JWT 토큰의 유효성 검사
    public static boolean validToken(String token) {
        try {
            // 토큰을 파싱하고 서명을 검증하여 유효성을 검사
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true; // 토큰이 유효함
        } catch (ExpiredJwtException e) {
            System.out.println("token is over");
            return false; // 토큰이 만료됨
        } catch (JwtException e) {
            System.out.println("token is invalid");
            return false; // 토큰이 유효하지 않음
        }
    }
}
