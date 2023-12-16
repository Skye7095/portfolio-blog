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
public class ReplyAddRequest {
	
	private int postId;
	private int replyId;
	private int userId;
	private String content;
}
