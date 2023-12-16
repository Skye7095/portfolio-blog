package com.portfolio.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.dto.Post;
import com.portfolio.blog.dto.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{

	// postId를 통해 해단 post의 댓글 조회
	List<Reply> findByPostId(int postId);
	
	// reply id를 통해 글 조회
	Optional<Reply> findById(int id);
}
