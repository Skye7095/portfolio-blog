package com.portfolio.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.blog.dto.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	// post 전체 조회
	@Override
	List<Post> findAll();
	
	// post id를 통해 글 조회
	Optional<Post> findById(int id);
	
	// 개인 글 조회
	List<Post> findByUserId(int userId);
	
}
