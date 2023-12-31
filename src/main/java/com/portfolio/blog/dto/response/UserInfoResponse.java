package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
	private int id;
	private String email;
	private String nickName;
	private String userImg;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
