package com.portfolio.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.dto.response.ApiResponse;
import com.portfolio.blog.dto.response.LikeResponse;
import com.portfolio.blog.dto.response.PostResponse;
import com.portfolio.blog.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Like", description="좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LikeController {
	
	private final LikeService likeService;
	
	// 좋아요 추가/취소
	@Operation(summary="좋아요 추가/취소", description="token 및 postId 필수 / likeResponse리턴되면 like상태, 리턴값 없을 시 unlike상태 /  url: /api/likes/toggle?postId=*")
	@PostMapping("/toggle")
	public ResponseEntity<ApiResponse> writeReply(Authentication authentication, @RequestParam int postId){      
        try {
        	likeService.toggleLike(authentication.getName(), postId);
	        return ResponseEntity.ok(new ApiResponse(true, "좋아요 성공적으로 등록/취소됐습니다."));
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
	    }
	}
	
	// 좋아요 조회
	@Operation(summary="좋아요 조회", description="postId필요 / url: /api/likes?postId=* / 최신순 정렬")
	@GetMapping("")
	public ResponseEntity<List<LikeResponse>> postLikes(@RequestParam int postId) {
		List<LikeResponse> likeResponses = likeService.getLikes(postId);
        return ResponseEntity.ok(likeResponses);
	}
	
}
