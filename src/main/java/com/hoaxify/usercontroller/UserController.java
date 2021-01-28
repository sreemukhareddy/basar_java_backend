package com.hoaxify.usercontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.user.GenericResponse;
import com.hoaxify.user.User;
import com.hoaxify.userservice.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	

	@PostMapping("/api/1.0/users")
	public ResponseEntity<GenericResponse> createUSer(@Valid @RequestBody User user) {
		userservice.saveUser(user);
		GenericResponse body = new GenericResponse("Success");
		return new ResponseEntity<GenericResponse>(body, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:9009"})
	@PostMapping("/api/1.0/login")
	public ResponseEntity<User> loginUser(Authentication authentication) {
		User loggedInUser = (User) authentication.getPrincipal();
		loggedInUser.setPassword(null);
		return new ResponseEntity<User>(loggedInUser, HttpStatus.OK);
	}
	
	@PostMapping("/api/2.0/login")
	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:9009"})
	public ResponseEntity<User> loginUserV2(@RequestBody User user) {
		User loggedInUser = userservice.loadUserByUsername(user.getUsername());
		loggedInUser.setPassword(null);
		return new ResponseEntity<User>(loggedInUser, HttpStatus.OK);
	}
}
