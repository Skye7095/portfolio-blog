package com.portfolio.blog.domain.dto.request;

import java.time.LocalDateTime;

import com.portfolio.blog.domain.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostAddRequest {
	
	private int userId;
	private String title;
	private String content;
	private String mainImg;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public Post toEntity() {
		return Post.builder()
				.userId(userId)
				.title(title)
				.content(content)
				.mainImg(mainImg)
				.createdAt(createdAt)
				.build();
	}
}
