package com.portfolio.blog.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.portfolio.blog.config.FileManagerService;
import com.portfolio.blog.dto.Post;
import com.portfolio.blog.dto.User;
import com.portfolio.blog.dto.request.PostAddRequest;
import com.portfolio.blog.dto.request.PostUpdateRequest;
import com.portfolio.blog.dto.response.LikeResponse;
import com.portfolio.blog.dto.response.PostResponse;
import com.portfolio.blog.dto.response.ReplyResponse;
import com.portfolio.blog.dto.response.UserInfoResponse;
import com.portfolio.blog.exception.AppException;
import com.portfolio.blog.exception.ErrorCode;
import com.portfolio.blog.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	private final UserService userService;
	private final ReplyService replyService;
	private final LikeService likeService;
	
	// id를 통해 post 조회
	public Post getPostById(int postId) {
		return postRepository.findById(postId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.POST_NOT_FOUND, postId + "번째 글은 찾을 수 없습니다.");
				});
	}
	
	// postResponse 객체 생성 
	public PostResponse postResponse(Post post, UserInfoResponse userInfoResponse, 
			List<ReplyResponse> replyResponses, List<LikeResponse> likeResponses) {
        return new PostResponse(post.getId(), post.getUserId(), userInfoResponse, 
        		post.getTitle(), post.getContent(), post.getMainImg(), 
        		replyResponses, likeResponses,
        		post.getCreatedAt(), post.getUpdatedAt());
    }
	
	// 글 등록
	public void writePost(
			String email
			, PostAddRequest dto) {
		// user 정보 찾기
		User user = userService.getUserByEmail(email);
		
		// mainImg 경로
		String mainImg = "null";
		if (dto.getFile() != null) {
			mainImg = FileManagerService.savePostFile(dto.getTitle(), dto.getFile());
		}
		
		// 저장
		Post post = Post.builder()
				.userId(user.getId())
				.title(dto.getTitle())
				.content(dto.getContent()) // 가입시 모두 user 부여
				.mainImg(mainImg)
				.build();
		postRepository.save(post);
	}

	// 글 전체 조회
	public List<PostResponse> getAllPost(){
		
		// 글의 작성시간으로 즉, id로 정렬
		Sort sort = Sort.by(Direction.DESC, "id");
		List<Post> postList = postRepository.findAll(sort);
		
		// Stream api 사용
		return postList.stream()
		        .map(post -> {
		        	List<ReplyResponse> replyResponses = replyService.getPostReplies(post.getId());
	                List<LikeResponse> likeResponses = likeService.getLikes(post.getId());

	                return new PostResponse(
	                        post,
	                        userService.getUserInfoById(post.getUserId()),
	                        replyResponses,
	                        likeResponses
	                );
	            })
		        .collect(Collectors.toList());
	}
	
	// 글 수정
	public void updatePost(
			String email
			, int postId
			, PostUpdateRequest dto) {
		
		// 만약 사용자가 없으면
		User user = userService.getUserByEmail(email);
		
		// 해당 id인 글이 존재 하지 않음
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.POST_NOT_FOUND, postId + "번째 글을 찾을 수 없습니다.");
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
		if(dto.getNewFile() != null ) {
			String mainImg = FileManagerService.savePostFile(dto.getNewTitle(), dto.getNewFile());
			post.setMainImg(mainImg);
		}
		
		// 업데이트시 업데이트 시간 추가
		postRepository.save(post);

	}
	
	// 개인 글 조회
	public List<PostResponse> getUserPost(int userId){
		
		List<Post> postList = postRepository.findByUserId(userId);
		
		// 글의 작성시간으로 즉, id로 정렬
		Comparator<PostResponse> postIdComparator = Comparator.comparing(PostResponse::getId, Comparator.reverseOrder());
		
		// Stream api 사용
		return postList.stream()
				.map(post -> {
					List<ReplyResponse> replyResponses = replyService.getPostReplies(post.getId());
	                List<LikeResponse> likeResponses = likeService.getLikes(post.getId());

	                return new PostResponse(
	                        post,
	                        userService.getUserInfoById(post.getUserId()),
	                        replyResponses,
	                        likeResponses
	                );
	            })
		        .sorted(postIdComparator)
		        .collect(Collectors.toList());
	}
	
	// 개별 글 조회
	public PostResponse getPost(int postId) {
		// 만약 글이 없으면
		Post post = getPostById(postId);
		
		// userInfoResponse 만들기
		UserInfoResponse userInfoResponse = userService.getUserInfoById(post.getUserId());
		
		// replyReponse
		List<ReplyResponse> replyResponses = replyService.getPostReplies(postId);
		
		// likeResponse
		List<LikeResponse> likeResponses = likeService.getLikes(postId);
		
		// postResponse 반환
		return postResponse(post, userInfoResponse, replyResponses, likeResponses);
	}
	
	// 개별 글 삭제
	public void deletePost(String email, int postId) {
		// 만약 사용자가 없으면
		User user = userService.getUserByEmail(email);
		
		// 해당 id인 글이 존재 하지 않음
		Post post = getPostById(postId);
		
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
