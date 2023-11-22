package com.portfolio.blog.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserTokenResponse {

	private int userId;
	private String accessToken;
	
//	public UserTokenResponse(Token entity) {
//		this.userId = entity.getUserId();
//		this.accessToken = entity.getAccessToken();
//	}
}
