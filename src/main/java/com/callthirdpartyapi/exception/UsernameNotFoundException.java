package com.callthirdpartyapi.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsernameNotFoundException extends RuntimeException {
	
	private String message;

	public UsernameNotFoundException(String message) {
		super(message);
		
	}
	
	

}
