package com.portfolio.blog.dto.request;

import java.time.LocalDateTime;

import com.portfolio.blog.dto.Post;

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

}
