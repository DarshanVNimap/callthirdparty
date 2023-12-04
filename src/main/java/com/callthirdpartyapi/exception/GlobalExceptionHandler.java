package com.callthirdpartyapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException ex){
		return ResponseEntity
							.status(HttpStatus.BAD_REQUEST)
							.body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex){
		return ResponseEntity
							.badRequest()
							.body(ex.getMessage());
	}
}
