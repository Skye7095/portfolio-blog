package com.portfolio.blog.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.blog.dto.request.UserUpdateRequest;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.UserRepository;
import com.portfolio.blog.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	// 비번 encoding
	private final BCryptPasswordEncoder encoder;
	
	// jwt토큰발행시 필요한 key. application.yml에 있음
	@Value("${jwt.secret}")
	private String key;
	
	// expireTimeMs = 5분
	private Long expireTimeMs = 1000 * 60 * 5l; 
	
	// 회원가입
	public String join(String email, String password) {
		
		// email 중복체크
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + " 이메일은 사용중입니다.");
			});
		
		
		// 저장
		User user = User.builder()
				.email(email)
				.password(encoder.encode(password))
				.role(Collections.singletonList("ROLE_USER")) // 가입시 모두 user 부여
				.build();
		userRepository.save(user);
		
		return "SUCCESS";
	}
	
	// 로그인
	public String login(String email, String password) {
		// email 없음
		User selectedUser = userRepository.findByEmail(email)
					.orElseThrow(() -> {
						throw new AppException(ErrorCode.EMAIL_NOT_FOUND, " 이메일은 존재하지 않습니다.");
					});
		// password 틀림
		if(!encoder.matches(password, selectedUser.getPassword())) {
			throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
		}
		
		// 토큰 발행
		String token = JwtUtil.createToken(selectedUser.getEmail(), key, expireTimeMs);
		return token;
	}
	
	// 정보 수정
	public String update(
			String email
			, UserUpdateRequest dto) {
		
		// 만약 사용자가 없으면
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.EMAIL_NOT_FOUND, email + "는 찾을 수 없는 회원입니다.");
				});
		
		// nickName 중복체크
		userRepository.findByNickName(dto.getNickName())
			.ifPresent(existingUser -> {
                if (existingUser.getId() != (user.getId())) {
                    throw new AppException(ErrorCode.NICKNAME_DUPLICATED, dto.getNickName() + " 닉네임은 사용중입니다.");
                }
			});
		
		// 수정 저장 
		user.setNickName(dto.getNickName());
		
		// 비번 변경하는 경우에만 비번 업데이트
		if(dto.getNewPw() != null) {
			user.setPassword(encoder.encode(dto.getNewPw()));
		}
		
		// userImg 변경하는 경우에만 비번 업데이트
		if(dto.getUserImg() != null ) {
			user.setUserImg(dto.getUserImg());
		}
		
		userRepository.save(user);
		
		return "SUCCESS";
	}
}
