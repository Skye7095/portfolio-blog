package com.portfolio.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.domain.dto.UserSignupRequest;
import com.portfolio.blog.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {
	
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> userSignUp(@RequestBody UserSignupRequest user) {
		userService.join(user.getLoginId(), user.getPassword(), user.getEmail());
		return ResponseEntity.ok().body("회원가입 이 성공했습니다.");
	}
}
