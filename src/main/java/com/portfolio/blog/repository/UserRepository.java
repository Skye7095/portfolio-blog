package com.portfolio.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// loginId 중복 체크
	Optional<User> findByLoginId(String loginId);
	
	// email 중복 체크
	Optional<User> findByEmail(String email);
}
