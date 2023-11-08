package com.portfolio.blog.domain.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostUpdateRequest {
	
	private String newTitle;
	private String newContent;
	private String newMainImg;
	private LocalDateTime updatedAt = LocalDateTime.now();
}
