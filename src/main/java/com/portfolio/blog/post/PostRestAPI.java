package com.portfolio.blog.post;

import java.awt.print.Book;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.blog.post.bo.PostBO;
import com.portfolio.blog.post.model.Post;

@RestController
@RequestMapping("/posts")
public class PostRestAPI {
	
	@Autowired
	private PostBO postBO;
	
	// 전체 글 가져오기
	@GetMapping()
	public List Posts() {
		
		List<Post> postList = postBO.getAllPostList();
		
		return postList;
	}
	
	// 개인 글 가져오기
	@GetMapping("/{userId}")
	public String PersonalPosts(@PathVariable String userId) {
		return "posts/userid";
	}
	
	// 새 글 생성하기
	@PostMapping("/add/{userId}")
	public String addPost(
			@RequestBody Book book,
			@PathVariable String userId) {
		return "add/userID";
	}
	
	// 글 수정하기
	@PutMapping("/update/{userId}")
	public String updatePost(
			@RequestBody Book book,
			@PathVariable String userId) {
		return "update/userid";
	}
	
	// 글 삭제하기
	@DeleteMapping("/delete/{postId}")
	public String deletePost(@PathVariable int postId) {
		return "delete/postid";
	}
}
