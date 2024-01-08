package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.blog.dto.Like;
import com.portfolio.blog.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikeResponse {
	public LikeResponse(Like like, UserInfoResponse userInfoById) {
		// TODO Auto-generated constructor stub
	}
	private int id;  // like의 id
	private int postId;
	private int userId;
	private UserInfoResponse userInfoResponse;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
