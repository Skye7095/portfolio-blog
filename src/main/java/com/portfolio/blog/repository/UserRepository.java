package com.portfolio.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.blog.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// loginId를 통해 user 찾기
	Optional<User> findByLoginId(String loginId);
	
	// email를 통해 user 찾기
	Optional<User> findByEmail(String email);
	
	// nickName를 통해 user 찾기
	Optional<User> findByNickName(String nickName);
	
	// id를 통해 user찾기
	Optional<User> findById(int id);
}
