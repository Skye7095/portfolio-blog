package com.portfolio.blog.post;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public List<Post> Posts() {
		
		List<Post> allPostList = postBO.getAllPostList();
		
		return allPostList;
	}
	
	// 개인 글 가져오기
	@GetMapping("/{userId}")
	public List<Post> PersonalPosts(@PathVariable String userId) {
		List<Post> postList = postBO.getPostList(userId);
		
		return postList;
	}
	
	// 새 글 생성하기
	@PostMapping("/add")
	public Map<String, String> addPost(
			@RequestParam("userId") String userId
			, @RequestParam("author") String author
			, @RequestParam("title") String title
			, @RequestParam("content") String content) {
		
		int count = postBO.addPost(userId, author, title, content);
		
		Map<String, String> result = new HashMap<>();
		
		if(count == 1) {
			result.put("addPost", "success");
		} else {
			result.put("addPost", "fail");
		}
		
		return result;
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
