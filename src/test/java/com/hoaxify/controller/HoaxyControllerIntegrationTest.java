package com.hoaxify.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hoaxify.user.GenericResponse;
import com.hoaxify.user.User;
import com.hoaxify.userrepository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HoaxyControllerIntegrationTest {
	
	private static final String API_1_0_USERS = "/api/1.0/users";
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	UserRepository userRepository;
	
	@Before
	public void init() {
		userRepository.deleteAll();
	}
	
	@Test
	public void postUser_whenUserIsValid_receiveOk() {
		User user = getValidUser();
		
		ResponseEntity<Object> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
		assertThat(postForEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	public void postUser_whenUserIsValid_saveToDatabase() {
		User user = getValidUser();
		
		this.testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
		assertThat(userRepository.count()).isEqualTo(1);
	}
	
	@Test
	public void postUser_whenUserIsValid_receiveSuccess() {
		User user = getValidUser();
		
		ResponseEntity<GenericResponse> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
		assertThat(postForEntity.getBody().getMessage()).isNotNull();
	}
	
	@Test
	public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
		User user = getValidUser();
		
		ResponseEntity<GenericResponse> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
		List<User> findAll = userRepository.findAll();
		User inDb = findAll.get(0);
		assertThat(inDb.getPassword()).isNotEqualTo(user.getPassword());
	}
	
	@Test
	public void postUser_whenUserIsInValid_badRequestException() {
		User user = getValidUser();
		user.setUsername(null);
		ResponseEntity<GenericResponse> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
		assertThat(postForEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserIsLessThanMinChars_badRequestException() {
		User user = getValidUser();
		user.setUsername("qwe");
		ResponseEntity<GenericResponse> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
		assertThat(postForEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void postUser_whenUserIsInValid_receiveApiError() {
		User user = new User();
		ResponseEntity<GenericResponse> postForEntity = this.testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);
		assertThat(postForEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private User getValidUser() {
		User user = new User();
		user.setDisplayName("userdispl");
		user.setUsername("usernameqwe");
		user.setPassword("P1ssword");
		return user;
	}

}
