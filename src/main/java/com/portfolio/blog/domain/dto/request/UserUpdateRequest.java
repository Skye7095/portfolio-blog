package com.portfolio.blog.domain.dto.request;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateRequest {

	private String newPw;
	private String nickName;
	private String userImg;
	private LocalDateTime updatedAt = LocalDateTime.now();
}
