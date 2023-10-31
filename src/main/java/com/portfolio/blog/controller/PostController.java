package com.portfolio.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.domain.dto.PostAddRequest;
import com.portfolio.blog.service.PostService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Post", description="글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
	
	private final PostService postService;
	
	// 글 등록
	@PostMapping("/add")
	public ResponseEntity<String> writePost(Authentication authentication, @RequestBody PostAddRequest dto){
//		postService.writePost(null, null, null, null, null)
		
		return ResponseEntity.ok().body(authentication.getName() + "글 등록되었습니다.");
	}
}
