package com.portfolio.blog.service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.portfolio.blog.dto.Like;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.dto.response.LikeResponse;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	
	private final LikeRepository likeRepository;
	private final UserService userService;
	
	@Autowired
	@Lazy
    private PostService postService;
	
	// likeResponse 객체 생성 
	public LikeResponse likeResponse(Like like, UserInfoResponse userInfoResponse) {
        return new LikeResponse(like.getId(), like.getUserId(), like.getPostId(), 
        		userInfoResponse, like.getCreatedAt());
    }
	
	// 좋아요 등록/취소
	public void toggleLike(String email, int postId) {
		
		// 만약 post가 없으면
		postService.getPostById(postId);
		
		// 만약 user가 없으면
		User user = userService.getUserByEmail(email);
		
		// postId와 userId를 통해 like여부 확인
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태이면 좋아요 취소
            likeRepository.deleteById(existingLike.get().getId());
        } else {
            // 좋아요를 누르지 않은 상태이면 좋아요 추가
        	Like like = Like.builder()
        			.userId(user.getId())
        			.postId(postId)
        			.build();
            likeRepository.save(like);
        }
    }
	
	// 좋아요 조회
	public List<LikeResponse> getLikes(int postId){
		
		// 만약 post가 없으면
		postService.getPostById(postId);
		
		List<Like> likeList = likeRepository.findByPostId(postId);
		
		// 댓글의 작성시간으로 즉, id로 정렬
		Comparator<LikeResponse> likeIdComparator = Comparator.comparing(LikeResponse::getId, Comparator.reverseOrder());
		
		// Stream api 사용
		return likeList.stream()
				.map(like -> new LikeResponse(like, userService.getUserInfoById(like.getUserId())))
		        .sorted(likeIdComparator)
		        .collect(Collectors.toList());
	}
}
