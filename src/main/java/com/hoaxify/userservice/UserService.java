package com.hoaxify.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.exception.CustomException;
import com.hoaxify.user.User;
import com.hoaxify.userrepository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	private UserRepository repository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		log.info("injecting up the dependencies into the UserService");
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		log.info("Completed injecting up the dependencies into the UserService");
	}
	
	public User saveUser(User user) {
		User existingUser = repository.findByUsername(user.getUsername());
		if(existingUser != null) {
			throw new CustomException("User is already present in the db");
		}
		log.info("saving the user into the db");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}
	
	public User loadUserByUsername(String username) {
		User user = repository.findByUsername(username);
		if( user == null) {
			throw new CustomException("User NotFound");
		}
		return user;
	}
	
	
}
