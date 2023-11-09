package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;

import com.portfolio.blog.dto.Post;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
public class PostResponse {
	
	private int id;  // post의 id
	private int userId;
	private String title;
	private String content;
	private String mainImg;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public PostResponse(Post entity) {
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.mainImg = entity.getMainImg();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}
}
