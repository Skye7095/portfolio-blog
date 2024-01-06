package com.portfolio.blog.service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portfolio.blog.dto.Like;
import com.portfolio.blog.dto.Post;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.dto.response.LikeResponse;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.LikeRepository;
import com.portfolio.blog.repository.PostRepository;
import com.portfolio.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	
	private final LikeRepository likeRepository;
	private final UserService userService;
	private final PostService postService;
	
	// 좋아요 등록/취소
	public LikeResponse toggleLike(String email, int postId) {
		
		// 만약 post가 없으면
		Post post = postService.getPostById(postId);
		
		// 만약 user가 없으면
		User user = userService.getUserByEmail(email);
		
		// postId와 userId를 통해 like여부 확인
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태이면 좋아요 취소
            likeRepository.deleteById(existingLike.get().getId());
            return null; // 좋아요 취소 상태
        } else {
            // 좋아요를 누르지 않은 상태이면 좋아요 추가
            Like like = new Like();
            like.setUserId(user.getId());
            like.setPostId(postId);
            likeRepository.save(like);
            
            // 사용자의 필수정보를 객체에 담아서 리턴
    		UserInfoResponse userInfoResponse = UserInfoResponse.builder()
    				.id(user.getId())
    				.email(user.getEmail())
    				.nickName(user.getNickName())
    				.userImg(user.getUserImg())
    				.createdAt(user.getCreatedAt())
    				.updatedAt(user.getUpdatedAt())
    				.build();
    		
    		LikeResponse likeResponse = new LikeResponse(like, userInfoResponse);
    		likeResponse.setId(like.getId());
    		likeResponse.setPostId(like.getPostId());
    		likeResponse.setUserId(like.getUserId());
    		likeResponse.setUserInfoResponse(userInfoResponse);
    		likeResponse.setCreatedAt(like.getCreatedAt());
            
            return likeResponse; // 좋아요 상태
        }
    }
	
	// 좋아요 조회
	public List<LikeResponse> getLikes(int postId){
		
		// 만약 post가 없으면
		Post post = postService.getPostById(postId);
		
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
