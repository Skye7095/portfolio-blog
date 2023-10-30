package com.portfolio.blog.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	LOGINID_DUPLICATED(HttpStatus.CONFLICT, ""),
	EMAIL_DUPLICATED(HttpStatus.CONFLICT, ""),
	LOGINID_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "");
	
	private HttpStatus httpStatus;
	private String message;
}
