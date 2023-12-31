package com.portfolio.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.dto.request.UserJoinRequest;
import com.portfolio.blog.dto.request.UserLoginRequest;
import com.portfolio.blog.dto.request.UserUpdateRequest;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.dto.response.UserTokenResponse;
import com.portfolio.blog.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="User", description="사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	
	private final UserService userService;
	
	// 회원가입
	@Operation(summary="회원가입")
	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody UserJoinRequest dto){
		userService.join(dto.getEmail(), dto.getPassword(), dto.getNickName(), dto.getFile());
		
		return ResponseEntity.ok().body("회원가입 성공했습니다.");
	}
	
	// 로그인
	@Operation(summary="로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest dto){
	    UserTokenResponse userTokenResponse = userService.login(dto.getEmail(), dto.getPassword());
	    return ResponseEntity.ok().body(userTokenResponse);
	}
	
	// 정보 수정
	@Operation(summary="회원 정보 수정", description="token필수")
	@PostMapping("/update")
	public ResponseEntity<?> update(Authentication authentication, @RequestBody UserUpdateRequest dto){		
		UserInfoResponse userInfoResponse = userService.update(authentication.getName(), dto);
	    return ResponseEntity.ok().body(userInfoResponse);
	}
	
	// 개별 사용자 조회
	@Operation(summary="사용자 개별 조회", description="userId필수 / url: /api/user?userId=* ")
	@GetMapping("")
    public ResponseEntity<UserInfoResponse> getUserById(@RequestParam int userId) {
		UserInfoResponse userInfoResponse = userService.getUserInfoById(userId);
		return ResponseEntity.ok().body(userInfoResponse);
    }
}
