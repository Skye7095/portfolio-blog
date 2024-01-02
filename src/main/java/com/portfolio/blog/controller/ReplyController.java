package com.portfolio.blog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	@Operation(summary="댓글 조회", description="postId필요 / url: /api/replies?postId=* / 최신순 정렬")
	@GetMapping("")
	public List<ReplyResponse> postReplies(@RequestParam int postId) {
		return replyService.getPostReplies(postId);
	}
	
	// 댓글 삭제
	@Operation(summary="댓글 삭제", description="token 및 replyId 필요 / replyId는 배열로 전달")
	@DeleteMapping("/delete")
	public String deletePost(Authentication authentication, @RequestBody Map<String, List<Integer>> requestBody) {
		List<Integer> replyIds = requestBody.get("replyIds");
		
		if(replyIds != null) {
			for (int replyId : replyIds) {
				replyService.deleteReply(authentication.getName(), replyId);
		    }
			return "선택하신 댓글을 삭제 완료했습니다.";
		}else {
			return "선택한 댓글이 없습니다";
		}
	}
}
