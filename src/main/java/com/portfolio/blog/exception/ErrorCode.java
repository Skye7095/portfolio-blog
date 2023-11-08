package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	EMAIL_DUPLICATED(HttpStatus.CONFLICT, ""),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
	INVALID_USER(HttpStatus.UNAUTHORIZED, ""),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
	NICKNAME_DUPLICATED(HttpStatus.CONFLICT, ""),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "")
	;
	
	private HttpStatus httpStatus;
	private String message;
}
