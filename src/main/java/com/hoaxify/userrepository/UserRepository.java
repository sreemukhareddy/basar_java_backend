package com.hoaxify.userrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
}
