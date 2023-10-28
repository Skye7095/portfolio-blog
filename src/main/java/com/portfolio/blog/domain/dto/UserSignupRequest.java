package com.portfolio.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignupRequest {
	private String loginId;
	private String password;
	private String email;
}
