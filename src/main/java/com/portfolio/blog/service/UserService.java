package com.portfolio.blog.service;

import org.springframework.stereotype.Service;

import com.portfolio.blog.domain.User;
import com.portfolio.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	// 회원가입
	public String join(String loginId, String password, String email) {
		
		// loginId 중복 체크
		userRepository.findByLoginId(loginId)
			.ifPresent(user -> {
				throw new RuntimeException(loginId + "는 이미 사용중입니다.");
			});
		
		// email 중복 체크
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new RuntimeException(email + "는 이미 가입했습니다.");
			});
		
		// 저장
		User user = User.builder()
				.loginId(loginId)
				.password(password)
				.email(email)
				.build();
		userRepository.save(user);
		
		return "success";
	}
}
