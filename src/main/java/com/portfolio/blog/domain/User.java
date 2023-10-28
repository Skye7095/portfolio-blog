package com.portfolio.blog.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String loginId;
	private String password;
	private String email;
	private String nickName;
	private String userImg;
	private Date createdAt;
	private Date updatedAt;
	
//	@Builder
//	public User(String loginId, String password, String email, String nickName,String userImg) {
//		
//		user.loginId = loginId;
//		user.password = password;
//		user.email = email;
//		user.nickName = nickName;
//		user.userImg = userImg;
//
//	}
}
