package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;

import com.portfolio.blog.dto.Post;
import com.portfolio.blog.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {
	
	private int id;  // post의 id
	private int userId;
	private UserInfoResponse userInfoResponse;
	private String title;
	private String content;
	private String mainImg;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public PostResponse(Post post, UserInfoResponse userInfoResponse) {
		this.id = post.getId();
		this.userId = post.getUserId();
		this.userInfoResponse = userInfoResponse;
		this.title = post.getTitle();
		this.content = post.getContent();
		this.mainImg = post.getMainImg();
		this.createdAt = post.getCreatedAt();
		this.updatedAt = post.getUpdatedAt();
	}
}
