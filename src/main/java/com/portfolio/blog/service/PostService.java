package com.portfolio.blog.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.portfolio.blog.domain.Post;
import com.portfolio.blog.domain.User;
import com.portfolio.blog.repository.PostRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;

	// 글 등록
	public String writePost(User user, String title, String content, String mainImg, Date createdAt) {
		
		// 저장
		Post post = Post.builder()
				.nickName(user.getNickName())
				.title(title)
				.content(content)
				.mainImg(mainImg)
				.createdAt(createdAt)
				.build();
		postRepository.save(post);
		
		return "SUCCESS";
	}
}
