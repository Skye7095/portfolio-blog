package com.portfolio.blog.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.blog.domain.User;
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
	
	// expireTimeMs = 1분
	private Long expireTimeMs = 1000 * 60l; 
	
	// 회원가입
	public String join(String loginId, String password, String email, Date createdAt) {
		
		// loginId 중복체크
		userRepository.findByLoginId(loginId)
			.ifPresent(user -> {
				throw new AppException(ErrorCode.LOGINID_DUPLICATED, loginId + " 아이디는 사용중입니다.");
			});
		
		// email 중복체크
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new AppException(ErrorCode.EMAIL_DUPLICATED, email + " 이메일은 사용중입니다.");
			});
		
		
		// 저장
		User user = User.builder()
				.loginId(loginId)
				.password(encoder.encode(password))
				.email(email)
				.createdAt(createdAt)
				.build();
		userRepository.save(user);
		
		return "SUCCESS";
	}
	
	// 로그인
	public String login(String loginId, String password) {
		// loginId 없음
		User selectedUser = userRepository.findByLoginId(loginId)
					.orElseThrow(() -> new AppException(ErrorCode.LOGINID_NOT_FOUND, " 아이디는 존재하지 않습니다."));
		// password 틀림
		if(!encoder.matches(password, selectedUser.getPassword())) {
			throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
		}
		
		// 토큰 발행
		String token = JwtUtil.createToken(selectedUser.getLoginId(), key, expireTimeMs);
		return token;
	}
}
