package com.portfolio.blog.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse {
	private boolean success;
    private String message;
    
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}