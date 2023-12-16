package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;

import com.portfolio.blog.dto.Reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyResponse {
	
	private int id;  // reply의 자동 id
	private int postId;
	private int replyId;	// 대댓글id
	private int userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public ReplyResponse(Reply entity) {
		this.id = entity.getId();
		this.postId = entity.getPostId();
		this.replyId = entity.getReplyId();
		this.userId = entity.getUserId();
		this.content = entity.getContent();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}
}
