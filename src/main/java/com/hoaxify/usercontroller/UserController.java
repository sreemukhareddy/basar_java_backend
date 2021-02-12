package com.hoaxify.usercontroller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.CurrentUser;
import com.hoaxify.exception.CustomException;
import com.hoaxify.user.GenericResponse;
import com.hoaxify.user.User;
import com.hoaxify.user.vm.UserUpdateVM;
import com.hoaxify.user.vm.UserVM;
import com.hoaxify.userservice.UserService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	@GetMapping("/api/1.0/users/{username}")
	public ResponseEntity<UserVM> getUserByUsername(@PathVariable String username, HttpServletRequest request) {
		log.info("url " + request.getServletPath());
		User user = userservice.getUserByUSername(username);
		if(user == null) {
			throw new CustomException("user is not found");
		}
		return new ResponseEntity<UserVM>(new UserVM(user), HttpStatus.OK);
	}
	
	@PutMapping("/api/1.0/users/{id:[0-9]+}")
	//@PreAuthorize("#id == principal.id")
	public ResponseEntity<UserVM> updateUSer(@PathVariable long id,@Valid @RequestBody(required = false) UserUpdateVM userUpdate) throws Exception {
		User user = userservice.update(id, userUpdate);
		return new ResponseEntity<UserVM>(new UserVM(user), HttpStatus.OK);
	}
	

	@PostMapping("/api/1.0/users")
	public ResponseEntity<GenericResponse> createUSer(@Valid @RequestBody User user) {
		userservice.saveUser(user);
		GenericResponse body = new GenericResponse("Success");
		return new ResponseEntity<GenericResponse>(body, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:9009"})
	@PostMapping("/api/1.0/login")
	public ResponseEntity<UserVM> loginUser(@CurrentUser User loggedInUser) {
		loggedInUser.setPassword(null);
		return new ResponseEntity<UserVM>(new UserVM(loggedInUser), HttpStatus.OK);
	}
	
	/*
	 
	 @CrossOrigin(origins = { "http://localhost:3000", "http://localhost:9009"})
	@PostMapping("/api/1.0/login")
	public ResponseEntity<UserVM> loginUser(Authentication authentication) {
		User loggedInUser = (User) authentication.getPrincipal();
		loggedInUser.setPassword(null);
		return new ResponseEntity<UserVM>(new UserVM(loggedInUser), HttpStatus.OK);
	}
	 */
	
	@PostMapping("/api/2.0/login")
	@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:9009"})
	public ResponseEntity<User> loginUserV2(@RequestBody User user) {
		User loggedInUser = userservice.loadUserByUsername(user.getUsername());
		loggedInUser.setPassword(null);
		return new ResponseEntity<User>(loggedInUser, HttpStatus.OK);
	}
	
	@GetMapping("/api/1.0/users")
	public Page<UserVM> getUsers(@CurrentUser User loggedInUser, Pageable pageable){
		/*
		 * Page<UserVM> map = userservice.getUsers().map( user -> { return new
		 * UserVM(user); });
		 */
		//userservice.getUsers().map(UserVM :: new);
		return userservice.getUsers(loggedInUser, pageable).map(UserVM :: new);
	}
}
