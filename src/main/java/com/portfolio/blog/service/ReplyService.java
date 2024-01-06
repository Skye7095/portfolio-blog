package com.portfolio.blog.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portfolio.blog.dto.Post;
import com.portfolio.blog.dto.Reply;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.dto.request.ReplyAddRequest;
import com.portfolio.blog.dto.response.ReplyResponse;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.PostRepository;
import com.portfolio.blog.repository.ReplyRepository;
import com.portfolio.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyService {
	
	private final ReplyRepository replyRepository;
	private final UserService userService;
	private final PostService postService;

	// 댓글 등록
	public void writeReply(String email, int postId, ReplyAddRequest dto) {
		
		// 만약 post가 없으면
		Post post = postService.getPostById(postId);
		
		// 만약 user가 없으면
		User user = userService.getUserByEmail(email);
		
		// 저장
		Reply reply = Reply.builder()
				.postId(postId)
				.replyId(dto.getReplyId())
				.userId(user.getId())
				.content(dto.getContent())
				.build();
		replyRepository.save(reply);
	}
	
	// 댓글 조회
	public List<ReplyResponse> getPostReplies(int postId){
		
		// 만약 post가 없으면
		Post post = postService.getPostById(postId);
		
		List<Reply> replyList = replyRepository.findByPostId(postId);
		
		// 댓글의 작성시간으로 즉, id로 정렬
		Comparator<ReplyResponse> replyIdComparator = Comparator.comparing(ReplyResponse::getId, Comparator.reverseOrder());
		
		// Stream api 사용
		return replyList.stream()
				.map(reply -> new ReplyResponse(reply, userService.getUserInfoById(reply.getUserId())))
		        .sorted(replyIdComparator)
		        .collect(Collectors.toList());
	}
	
	// 개별 댓글 삭제
	public void deleteReply(String email, int replyId) {
		// 만약 사용자가 없으면
		User user = userService.getUserByEmail(email);
		
		// 해당 id인 댓글이 존재 하지 않음
		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.REPLY_NOT_FOUND, Integer.toString(replyId) + "번째 댓글을 찾을 수 없습니다.");
				});
		
		// 해당 id인 댓글이 존재하지만 userId 일치하지 않을 경우 -> 권한 없음
		replyRepository.findById(replyId)
			.ifPresent(existingReply -> {
	            if (existingReply.getUserId() != (user.getId())) {
	            	// 해당 글이 해당 userId인 사용자가 작성한 거 아님
	                throw new AppException(ErrorCode.INVALID_USER, "해당 유저는 해당 댓글에 수정 권한이 없습니다.");
	            }
			});
		
		// 삭제
		replyRepository.delete(reply);
	}
}
