package com.portfolio.blog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.dto.request.ReplyAddRequest;
import com.portfolio.blog.dto.response.ReplyResponse;
import com.portfolio.blog.service.ReplyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Reply", description="댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/replies")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReplyController {

	private final ReplyService replyService;
	
	// 댓글 등록
	@Operation(summary="댓글 작성하기", description="token필수")
	@PostMapping("/add")
	public ResponseEntity<?> writeReply(Authentication authentication, @RequestBody ReplyAddRequest dto){	
		ReplyResponse replyResponse = replyService.writeReply(dto.getPostId(), dto.getReplyId(), authentication.getName(), dto.getContent());
		
		return ResponseEntity.ok().body(replyResponse);
	}
	
	// 댓글 조회
	@Operation(summary="댓글 조회", description="postId 필요 / 대댓글 작성 시, 최상위 replyId 전달 필요 / 최신순 정렬")
	@GetMapping("")
	public List<ReplyResponse> postReplies(@RequestParam int postId) {
		return replyService.getPostReplies(postId);
	}
	
	// 댓글 삭제
	@Operation(summary="댓글 삭제", description="token 및 replyId 필요 / replyId는 배열로 전달")
	@DeleteMapping("/delete")
	public String deletePost(Authentication authentication, @RequestParam List<Integer> replyIds) {
		for (int replyId : replyIds) {
			replyService.deleteReply(authentication.getName(), replyId);
	    }
		return "선택하신 댓글을 삭제 완료했습니다.";
	}
}
