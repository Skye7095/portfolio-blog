package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;

import com.portfolio.blog.dto.Like;
import com.portfolio.blog.dto.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponse {
	private int id;  // like의 id
	private int postId;
	private int userId;
	private UserInfoResponse userInfoResponse;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public LikeResponse(Like like, UserInfoResponse userInfoResponse) {
		this.id = like.getId();
		this.postId = like.getPostId();
		this.userId = like.getUserId();
		this.userInfoResponse = userInfoResponse;
		this.createdAt = like.getCreatedAt();
		this.updatedAt = like.getUpdatedAt();
	}
}
