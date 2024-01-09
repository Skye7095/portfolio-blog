package com.portfolio.blog.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.blog.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
	
	public PostResponse(Post post, UserInfoResponse userInfoResponse, List<ReplyResponse> replyResponses, List<LikeResponse> likeResponses) {
		// TODO Auto-generated constructor stub
		this.id = post.getId();
		this.userId = post.getUserId();
		this.userInfoResponse = userInfoResponse;
		this.title = post.getTitle();
		this.mainImg = post.getMainImg();
		this.content = post.getContent();
		this.replyResponses = replyResponses;
		this.likeResponses = likeResponses;
		this.createdAt = post.getCreatedAt();
		this.updatedAt = post.getUpdatedAt();
	}
	private int id;  // post의 id
	private int userId;
	private UserInfoResponse userInfoResponse;
	private String title;
	private String content;
	private String mainImg;
	private List<ReplyResponse> replyResponses;
	private List<LikeResponse> likeResponses;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
