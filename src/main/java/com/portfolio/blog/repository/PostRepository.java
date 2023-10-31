package com.portfolio.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
