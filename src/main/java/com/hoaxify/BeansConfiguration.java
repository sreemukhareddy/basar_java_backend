package com.hoaxify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfiguration {
	
	@Bean
	public PasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
