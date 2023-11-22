package com.portfolio.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.dto.Token;
import com.portfolio.blog.dto.User;

@Repository
public interface JWTRepository extends JpaRepository<Token, Integer>{
	
	// userId를 통해 해당 refreshToken찾기
	Optional<Token> findByUserId(int userId);
	
	// email을 통해 해당 refreshToken 찾기
	static Optional<Token> findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
}
