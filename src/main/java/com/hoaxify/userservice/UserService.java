package com.hoaxify.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.exception.CustomException;
import com.hoaxify.user.User;
import com.hoaxify.user.vm.UserUpdateVM;
import com.hoaxify.userrepository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	private UserRepository repository;
	private PasswordEncoder passwordEncoder;
	private FileService fileService;

	@Autowired
	public UserService(UserRepository repository, PasswordEncoder passwordEncoder, FileService fileService) {
		log.info("injecting up the dependencies into the UserService");
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.fileService = fileService;
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

	public Page<User> getUsers(User loggedInUser, Pageable pageable) {
		//Pageable pageable = PageRequest.of(page);
		if(loggedInUser != null && loggedInUser.getUsername() != null) {
			return repository.findByUsernameNot(loggedInUser.getUsername(), pageable);
		}
		return repository.findAll(pageable);
		//Page<UserProjection> usersProjection = repository.getAllUsersProjection(pageable);
		//usersProjection.
	}

	public User getUserByUSername(String username) {
		User user = repository.findByUsername(username);
		return user;
	}

	public User update(long id, UserUpdateVM userUpdate) throws Exception {
		User user = repository.getOne(id);
		user.setDisplayName(userUpdate.getDisplayName());
		String savedImage = null;
		
		if(userUpdate.getImage() != null) {
			savedImage = fileService.saveProfileImage(userUpdate.getImage());
			fileService.deleteProfileImage(user.getImage()); // if throws exception, comment this line of code
			user.setImage(savedImage);
		}
		
		return repository.save(user);
	}
	
	
}
