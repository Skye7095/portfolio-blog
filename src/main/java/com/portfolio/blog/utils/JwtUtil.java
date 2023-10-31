package com.portfolio.blog.utils;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	// 토큰에서 user정보 꺼내기
	public static String getEmail(String token, String key) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
				.getBody().get("email", String.class);
	}
	
	// 토큰 유효기간 넘음
	public static boolean isExpired(String token, String key) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token)
				.getBody().getExpiration().before(new Date());
	}
	
	// 토큰 발행
	public static String createToken(String email, String key, long expireTimeMs) {
		Claims claims = Jwts.claims(); 
		claims.put("email", email);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
	}
}
