package com.portfolio.blog.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.portfolio.blog.domain.Post;
import com.portfolio.blog.domain.User;
import com.portfolio.blog.domain.dto.request.PostAddRequest;
import com.portfolio.blog.domain.dto.request.PostUpdateRequest;
import com.portfolio.blog.domain.dto.response.PostResponse;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.PostRepository;
import com.portfolio.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	
	// 글 등록
	public int writePost(PostAddRequest dto) {
		// 저장
		Post post = postRepository.save(dto.toEntity());
		
		return post.getId();
	}
	
	
	// 글 전체 조회
	public List<PostResponse> getAllPost(){
		
		// 글의 작성시간으로 즉, id로 정렬
		Sort sort = Sort.by(Direction.DESC, "id");
		List<Post> postList = postRepository.findAll(sort);
		
		// Stream api 사용
		return postList.stream().map(PostResponse::new).collect(Collectors.toList());
	}
	
	// 글 수정
	public String updatePost(
			String email
			, int postId
			, PostUpdateRequest dto) {
		
		// 만약 사용자가 없으면
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.EMAIL_NOT_FOUND, email + "는 찾을 수 없는 회원입니다.");
				});
		
		// 해당 id인 글이 존재 하지 않음
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.POST_NOT_FOUND, Integer.toString(postId) + "번째 글을 찾을 수 없습니다.");
				});
		
		// 해당 id인 글이 존재하지만 userId 일치하지 않을 경우
		postRepository.findById(postId)
			.ifPresent(existingPost -> {
	            if (existingPost.getUserId() != (user.getId())) {
	            	// 해당 글이 해당 userId인 사용자가 작성한 거 아님
	                throw new AppException(ErrorCode.INVALID_USER, "해당 유저는 해당 글에 수정 권한이 없습니다.");
	            }
			});
		
		// title 변경하는 경우에만 제목 업데이트
		if(dto.getNewTitle() != null) {
			post.setTitle(dto.getNewTitle());
		}
		
		// content 변경하는 경우에만 content 업데이트
		if(dto.getNewContent() != null ) {
			post.setContent(dto.getNewContent());
		}
		
		// mainImg 변경하는 경우에만 mainImg 업데이트
		if(dto.getNewMainImg() != null ) {
			post.setContent(dto.getNewMainImg());
		}
		
		// 업데이트시 업데이트 시간 추가
		post.setUpdatedAt(dto.getUpdatedAt());
		postRepository.save(post);
			
		return "SUCCESS";
	}
	
	// 개인 글 조회
	public List<PostResponse> getUserPost(int userId){
		
		// 만약 사용자가 없으면
		User user = userRepository.findById(userId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.USER_NOT_FOUND, userId + "는 찾을 수 없는 회원입니다.");
				});
		
		List<Post> postList = postRepository.findByUserId(userId);
		
		// 글의 작성시간으로 즉, id로 정렬
		Comparator<PostResponse> postIdComparator = Comparator.comparing(PostResponse::getId, Comparator.reverseOrder());
		
		// Stream api 사용
		return postList.stream()
				.map(PostResponse::new)
		        .sorted(postIdComparator)
		        .collect(Collectors.toList());
	}
	
	// 개별 글 조회
	public Post getPost(int postId) {
		// 만약 글이 없으면
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> {
				throw new AppException(ErrorCode.POST_NOT_FOUND, postId + "번째 글은 찾을 수 없습니다.");
			});
		
		return post;
	}
	
	// 개별 글 삭제
	public void deletePost(String email, int postId) {
		// 만약 사용자가 없으면
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.EMAIL_NOT_FOUND, email + "는 찾을 수 없는 회원입니다.");
				});
		
		// 해당 id인 글이 존재 하지 않음
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.POST_NOT_FOUND, Integer.toString(postId) + "번째 글을 찾을 수 없습니다.");
				});
		
		// 해당 id인 글이 존재하지만 userId 일치하지 않을 경우 -> 권한 없음
		postRepository.findById(postId)
			.ifPresent(existingPost -> {
	            if (existingPost.getUserId() != (user.getId())) {
	            	// 해당 글이 해당 userId인 사용자가 작성한 거 아님
	                throw new AppException(ErrorCode.INVALID_USER, "해당 유저는 해당 글에 수정 권한이 없습니다.");
	            }
			});
		
		// 삭제
		postRepository.delete(post);
	}
}
