package com.portfolio.blog.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.post.model.Post;

@Repository
public interface PostDAO {
	
	// 전체 글 가져오기
	public List<Post> selectAllPostList();
	
	// 유저 별 글 생성
	public int insertPost(
			@Param("userId") String userId
			, @Param("author") String author
			, @Param("title") String title
			, @Param("content") String content);
}
