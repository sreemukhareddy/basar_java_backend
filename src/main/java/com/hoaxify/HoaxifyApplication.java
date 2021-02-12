package com.hoaxify;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.hoaxify.user.User;
import com.hoaxify.userservice.UserService;

@SpringBootApplication
public class HoaxifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoaxifyApplication.class, args);
	}
	
	@Bean
	@Profile("!test")
	CommandLineRunner run(UserService userservice) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				IntStream
						 .rangeClosed(1, 15)
						.mapToObj(i -> {
							User u = new User();
							u.setUsername("username" + i);
							u.setDisplayName("username" + i);
							u.setPassword("password" + i);
							return u;
						}).forEach(userservice :: saveUser);
			}
		};
	}

}
