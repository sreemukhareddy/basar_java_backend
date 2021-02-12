package com.hoaxify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hoaxify.userservice.AuthUserService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthUserService authUerService;
	
	@Autowired
	private PasswordEncoder passwordEncode;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().disable();
		http.httpBasic().authenticationEntryPoint(new BasicAuthenticationEntryPoint());
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/1.0/login").authenticated()
				.antMatchers(HttpMethod.POST, "/api/1.0/hoaxes/**").authenticated()
				//.antMatchers(HttpMethod.PUT, "/api/1.0/users/{id:[0-9]+}").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authUerService).passwordEncoder(passwordEncode);
	}
}
