package com.hoaxify.exception;

import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiError {

	private long timeStamp = new Date().getTime();
	
	private int status;
	
	private String message;
	
	private String url;
	
	private Map<String, String> errors;

	public ApiError(int status, String message, String url) {
		this.status = status;
		this.message = message;
		this.url = url;
	}
	
	
}
