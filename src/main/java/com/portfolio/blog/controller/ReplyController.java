package com.portfolio.blog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.portfolio.blog.dto.response.ApiResponse;
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
	public ResponseEntity<ApiResponse> writeReply(Authentication authentication, @RequestBody ReplyAddRequest dto){
		try {
			replyService.writeReply(authentication.getName(), dto.getPostId(), dto);
	        return ResponseEntity.ok(new ApiResponse(true, "글을 성공적으로 등록했습니다."));
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "글 등록 실패했습니다."));
	    }
	}
	
	// 댓글 조회
	@Operation(summary="댓글 조회", description="postId필요 / url: /api/replies?postId=* / 최신순 정렬")
	@GetMapping("")
	public ResponseEntity<List<ReplyResponse>> postReplies(@RequestParam int postId) {
		List<ReplyResponse> replyResponses = replyService.getPostReplies(postId);
		return ResponseEntity.ok().body(replyResponses);
	}
	
	// 댓글 삭제
	@Operation(summary="댓글 삭제", description="token 및 replyId 필요 / replyId는 배열로 전달 / url: /api/posts/delete?replyIds=*")
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse> deletePost(Authentication authentication, @RequestParam List<Integer> replyIds) {
	
		try {
			for (int replyId : replyIds) {
				replyService.deleteReply(authentication.getName(), replyId);
		    }
			return ResponseEntity.ok(new ApiResponse(true, "선택하신 댓글을 삭제 완료했습니다."));
		} catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "글 삭제 실패했습니다."));
	    }
	}
}