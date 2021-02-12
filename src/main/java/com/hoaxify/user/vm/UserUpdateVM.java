package com.hoaxify.user.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserUpdateVM {

	@NotNull
	@Size(max = 255, min = 4, message = "validation failed at displayname, must be 4 or less than 256 chars")
	private String displayName;
	
	private String image;
}
