package com.portfolio.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.domain.dto.UserJoinRequest;
import com.portfolio.blog.domain.dto.UserLoginRequest;
import com.portfolio.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final UserService userService;
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
		userService.join(dto.getLoginId(), dto.getPassword(), dto.getEmail(), dto.getCreatedAt());
		
		return ResponseEntity.ok().body("회원가입 성공했습니다.");
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginRequest dto){
		String token = userService.login(dto.getLoginId(), dto.getPassword());
		return ResponseEntity.ok().body(token);
	}
}
