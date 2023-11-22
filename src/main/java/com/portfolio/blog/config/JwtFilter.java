package com.portfolio.blog.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.blog.service.UserService;
import com.portfolio.blog.utils.JwtUtil;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final UserService userService;
	private final String key;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		// 토큰이 없을 시
		if(authorization == null || !authorization.startsWith("Bearer ")) {			
			try{
				filterChain.doFilter(request, response);
				return;
			}catch(Exception ex) {
				response.setHeader("error", ex.getMessage());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", ex.getMessage());
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		}
	
		// 토큰 꺼내기
		String token = authorization.split(" ")[1];
		
		// 토큰이 expired되었는지 체크
		if(JwtUtil.isExpired(token, key)) {
			// true반환, 즉 만료됨
			filterChain.doFilter(request, response);
			return;
		}
		
		// email를 token에서 꺼내기
		String email = JwtUtil.getEmail(token, key);
		
		// 권한 부여
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
		
		// detail를 넣어줌
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
