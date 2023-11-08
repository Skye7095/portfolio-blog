package com.portfolio.blog.domain.dto.request;

import java.time.LocalDateTime;

import com.portfolio.blog.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserJoinRequest {
	private String email;
	private String password;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public User toEntity() {
		return User.builder()
				.email(email)
				.password(password)
				.createdAt(createdAt)
				.build();
	}
}
