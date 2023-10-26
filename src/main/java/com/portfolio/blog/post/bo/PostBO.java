package com.portfolio.blog.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portfolio.blog.post.dao.PostDAO;
import com.portfolio.blog.post.model.Post;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	// 전체 글 조회
	public List<Post> getAllPostList(){
		return postDAO.selectAllPostList();
	}
	
	// 유저별 글 생성
	public int addPost(
			String userId
			, String author
			, String title
			, String content) {
		return postDAO.insertPost(userId, author, title, content);
	}
}
