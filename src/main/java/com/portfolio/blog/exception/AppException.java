package com.portfolio.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException{
	private ErrorCode errorCode;
	private String message;
}
