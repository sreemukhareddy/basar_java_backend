package com.hoaxify.exception.handler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hoaxify.exception.ApiError;
import com.hoaxify.exception.CustomException;

@RestControllerAdvice
public class HoaxifyExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> getResponse(MethodArgumentNotValidException exception, HttpServletRequest request) {
		ApiError api = new ApiError(400, "Bad request", request.getServletPath());
		BindingResult errors = exception.getBindingResult();
		Map<String, String> mapErrors = new HashMap<>();
		for(FieldError error : errors.getFieldErrors()) {
			mapErrors.put(error.getField(), error.getDefaultMessage());
		}
		api.setErrors(mapErrors);
		return new ResponseEntity<ApiError>(api, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiError> getResponse(CustomException exception, HttpServletRequest request) {
		ApiError api = new ApiError(500, exception.getMessage(), request.getServletPath());
		Map<String, String> mapErrors = new HashMap<>();
		mapErrors.put("error", exception.getMessage());
		api.setErrors(mapErrors);
		return new ResponseEntity<ApiError>(api, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({AccessDeniedException.class})
	public ResponseEntity<ApiError> getResponse(AccessDeniedException exception, HttpServletRequest request) {
		ApiError api = new ApiError(401, exception.getMessage(), request.getServletPath());
		Map<String, String> mapErrors = new HashMap<>();
		mapErrors.put("error", exception.getMessage());
		api.setErrors(mapErrors);
		return new ResponseEntity<ApiError>(api, HttpStatus.UNAUTHORIZED);
	}

}
