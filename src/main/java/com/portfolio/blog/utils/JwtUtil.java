package com.portfolio.blog.utils;

import java.util.Date;
import java.util.Optional;

import com.portfolio.blog.dto.Token;
import com.portfolio.blog.repository.JWTRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {
	
	// 토큰에서 user정보 꺼내기
	public static String getEmail(String token, String key) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
				.getBody().get("email", String.class);
	}
	
	// 토큰 유효기간 지남
	public static boolean isExpired(String token, String key) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
				.getBody().getExpiration().before(new Date(System.currentTimeMillis()));
	}
	
	// access 토큰 발행
	public static String createAccessToken(String email, String key) {
		Claims claims = Jwts.claims(); 
		claims.put("email", email);
		
		// 유효기간 30분
		long expirationTimeMillis = 1000 * 60 * 30;
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
	}
	
	// refresh 토큰 발행
	public static String createRefreshToken(String email, String key) {
		Claims claims = Jwts.claims(); 
		claims.put("email", email);
	
		// 유효기간 1시간
		long expirationTimeMillis = 1000 * 60 * 60;
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
	}
	
	// 토큰 검증
	public Boolean tokenValidation(String token, String key) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
	 
	// refreshToken 검증
	public Boolean refreshTokenValidation(String token, String key) {

        // 1차 토큰 검증
        if(!tokenValidation(token, key)) return false;

        // DB에 저장한 토큰 비교
        String email = getEmail(token, key);
        Optional<Token> refreshToken = JWTRepository.findByEmail(email);

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }
	
	// 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }
    
}
