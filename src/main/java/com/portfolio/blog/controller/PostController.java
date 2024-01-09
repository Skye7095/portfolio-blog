package com.portfolio.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.dto.request.PostAddRequest;
import com.portfolio.blog.dto.request.PostUpdateRequest;
import com.portfolio.blog.dto.response.ApiResponse;
import com.portfolio.blog.dto.response.PostResponse;
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
	public ResponseEntity<List<PostResponse>> allPosts() {
        List<PostResponse> postResponses = postService.getAllPost();
        return ResponseEntity.ok(postResponses);
    }
	
	// 글 등록
	@Operation(summary="글 작성하기", description="token필수")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addPost(Authentication authentication, @RequestBody PostAddRequest dto){
		try {
			postService.writePost(authentication.getName(), dto);
	        return ResponseEntity.ok(new ApiResponse(true, "글을 성공적으로 등록했습니다."));
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "글 등록 실패했습니다."));
	    }
	}
	
	// 글 수정
	@Operation(summary="글 수정하기", description="token, postId필수 / url: /api/posts/update?postId=*")
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> updatePost(Authentication authentication, @RequestParam int postId, @RequestBody PostUpdateRequest dto){
		try {
			postService.updatePost(authentication.getName(), postId, dto);
	        return ResponseEntity.ok(new ApiResponse(true, "글을 성공적으로 수정했습니다."));
	    } catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "글 수정 실패했습니다."));
	    }
	}
	
	// 개인 글 조회
	@Operation(summary="개인 글 조회", description="userId 필요 / url: /api/posts/getpostby?userId=*")
	@GetMapping("/getpostby")
	public ResponseEntity<List<PostResponse>> userPosts(@RequestParam int userId) {
		List<PostResponse> postResponses = postService.getUserPost(userId);
        return ResponseEntity.ok(postResponses);
	}
	
	// 개별 글 조회
	@Operation(summary="개별 글 조회", description="postId 필요 / url: /api/posts/getpost?postId=*")
	@GetMapping("/getpost")
	public ResponseEntity<PostResponse> getPost(@RequestParam int postId){
		PostResponse postResponse = postService.getPost(postId);
        return ResponseEntity.ok(postResponse);
	}
	
	// 글 삭제
	@Operation(summary="글 삭제", description="token 및 postId 필요 / url: /api/posts/delete?postIds=*")
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse> deletePost(Authentication authentication, @RequestParam List<Integer> postIds) {
		try {
			for (int postId : postIds) {
				postService.deletePost(authentication.getName(), postId);
		    }
			return ResponseEntity.ok(new ApiResponse(true, "선택하신 글을 삭제 완료했습니다."));
		} catch (Exception e) {
	        // 예외가 발생하면 실패 상태로 응답
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "글 삭제 실패했습니다."));
	    }
	}
}
