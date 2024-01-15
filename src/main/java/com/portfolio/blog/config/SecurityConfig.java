package com.portfolio.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.portfolio.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserService userService;
	
	// jwt토큰발행시 필요한 key. application.yml에 있음
	@Value("${jwt.secret}")
	private String key;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.httpBasic().disable()
				.csrf().disable()
				.cors().and()
				.authorizeRequests()
				.requestMatchers("/api/user/join", "/api/user/login", "/api/posts/add").permitAll()
//				.requestMatchers("/api/user/update").authenticated()
//				.requestMatchers("/api/posts/add").authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용하는 경우
				.and()
				.addFilterBefore(new JwtFilter(userService, key), UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
