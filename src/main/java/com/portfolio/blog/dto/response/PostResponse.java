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
	
	public PostResponse(Post post, UserInfoResponse userInfoById, List<ReplyResponse> replyResponses2,
			List<LikeResponse> likeResponses) {
		// TODO Auto-generated constructor stub
	}
	private int id;  // post의 id
	private int userId;
	private UserInfoResponse userInfoResponse;
	private String title;
	private String content;
	private String mainImg;
	private List<ReplyResponse> replyResponses;
	private List<LikeResponse> likeResponsess;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
