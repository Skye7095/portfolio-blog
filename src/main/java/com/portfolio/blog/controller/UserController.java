package com.portfolio.blog.controller;

import org.springframework.http.HttpStatus;
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
import com.portfolio.blog.dto.response.ApiResponse;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.dto.response.UserTokenResponse;
import com.portfolio.blog.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<?> join(@RequestBody UserJoinRequest dto){
		try {
			userService.join(dto.getEmail(), dto.getPassword(), dto.getNickName(), dto.getFile());
			return ResponseEntity.ok().body("회원가입 성공했습니다.");
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
	    }
	}
	
	// 로그인
	@Operation(summary="로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest dto){
		try {
			UserTokenResponse userTokenResponse = userService.login(dto.getEmail(), dto.getPassword());
		    return ResponseEntity.ok().body(userTokenResponse);
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
	    }	
	}
	
	// 정보 수정
	@Operation(summary="회원 정보 수정", description="token필수")
	@PostMapping("/update")
	public ResponseEntity<?> update(Authentication authentication, @RequestBody UserUpdateRequest dto){
		try {
	        UserInfoResponse userInfoResponse = userService.update(authentication.getName(), dto);
		    return ResponseEntity.ok().body(userInfoResponse);
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
	    }		
	}
	
	// 개별 사용자 조회
	@Operation(summary="사용자 개별 조회", description="userId필수 / url: /api/user?userId=* ")
	@GetMapping("")
    public ResponseEntity<?> getUserById(@RequestParam int userId) {
		try {
			UserInfoResponse userInfoResponse = userService.getUserInfoById(userId);
			return ResponseEntity.ok().body(userInfoResponse);
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
	    }	
    }
}
