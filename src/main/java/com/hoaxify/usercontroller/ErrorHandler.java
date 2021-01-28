package com.hoaxify.usercontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.hoaxify.exception.ApiError;

@RestController
public class ErrorHandler implements ErrorController {
	
	@Autowired
	private ErrorAttributes errorAttributes;
	
	@RequestMapping("/error")
	public ResponseEntity<ApiError> getResponse(WebRequest webRequest) {
		Map<String, Object> attributes = errorAttributes.getErrorAttributes(webRequest, true);
		//errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(Include.me));
		
		String message = (String) attributes.get("message");
		String url = (String) attributes.get("path");
		int status = (Integer) attributes.get("status");
		ApiError api = new ApiError(401, message, url);
		Map<String, String> mapErrors = new HashMap<>();
		mapErrors.put("error", message);
		api.setErrors(mapErrors);
		return new ResponseEntity<ApiError>(api, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
