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
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.dto.request.PostAddRequest;
import com.portfolio.blog.dto.request.PostUpdateRequest;
import com.portfolio.blog.dto.response.PostResponse;
import com.portfolio.blog.dto.response.UserTokenResponse;
import com.portfolio.blog.dto.Post;
import com.portfolio.blog.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Post", description="글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {
	
	private final PostService postService;
	
	// 전체 글 조회
	@Operation(summary="전체 글 조회", description="최신순 정렬")
	@GetMapping("")
	public List<PostResponse> allPosts() {
		return postService.getAllPost();
	}
	
	// 글 등록
	@Operation(summary="글 작성하기")
	@PostMapping("/post/add")
	public ResponseEntity<?> writePost(@RequestBody PostAddRequest dto){	
		PostResponse postResponse = postService.writePost(dto.getUserId(), dto.getTitle(), dto.getContent(), dto.getFile());
		
		return ResponseEntity.ok().body(postResponse);
	}
	
	// 글 수정
	@Operation(summary="글 수정하기")
	@PostMapping("/post/update/{postId}")
	public ResponseEntity<String> updatePost(Authentication authentication, @PathVariable int postId, @RequestBody PostUpdateRequest dto){
		postService.updatePost(authentication.getName(), postId, dto);
		return ResponseEntity.ok().body(postId + "번째 글 수정 성공했습니다.");
	}
	
	// 개인 글 조회
	@Operation(summary="개인 글 조회", description="userId 필요")
	@GetMapping("/user/{userId}")
	public List<PostResponse> userPosts(@PathVariable int userId) {
		return postService.getUserPost(userId);
	}
	
	// 개별 글 조회
	@Operation(summary="개별 글 조회", description="postId 필요")
	@GetMapping("/post/{postId}")
	public Post getPost(@PathVariable int postId){
		return postService.getPost(postId);
	}
	
	// 글 삭제
	@Operation(summary="개별 글 삭제", description="token 및 postId 필요")
	@DeleteMapping("/post/{postId}/delete")
	public String deletePost(Authentication authentication, @PathVariable int postId) {
		postService.deletePost(authentication.getName(), postId);
		return postId + "번째 글 삭제 완료했습니다.";
	}
}
