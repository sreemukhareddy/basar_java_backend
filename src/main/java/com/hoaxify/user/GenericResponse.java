package com.hoaxify.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse {

	private String message;

	public GenericResponse(String message) {
		// super();
		this.message = message;
	}

}
