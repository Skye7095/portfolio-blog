package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.blog.dto.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReplyResponse {
	
	public ReplyResponse(Reply reply, UserInfoResponse userInfoResponse) {
		// TODO Auto-generated constructor stub
		this.id = reply.getId();
		this.postId = reply.getPostId();
		this.originReplyId = reply.getOriginReplyId();
		this.userId = reply.getUserId();
		this.userInfoResponse = userInfoResponse;
		this.content = reply.getContent();
		this.createdAt = reply.getCreatedAt();
		this.updatedAt = reply.getUpdatedAt();
	}
	private int id;  // reply의 자동 id
	private int postId;
	private int originReplyId;	// 대댓글id
	private int userId;
	private UserInfoResponse userInfoResponse;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
