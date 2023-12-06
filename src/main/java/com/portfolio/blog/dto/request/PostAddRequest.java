package com.portfolio.blog.dto.request;

import org.springframework.web.multipart.MultipartFile;

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
	private MultipartFile file;

}
