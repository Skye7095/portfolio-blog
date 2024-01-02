package com.portfolio.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.dto.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
	
	// postId과 userId를 통해 동시에 like상태 조회
	Optional<Like> findByUserIdAndPostId(int userId, int postId);
	
	// postId를 통해 해단 post의 like 조회
	List<Like> findByPostId(int postId);
}
