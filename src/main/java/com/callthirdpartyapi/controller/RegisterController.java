package com.callthirdpartyapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.callthirdpartyapi.dto.ClientRequest;
import com.callthirdpartyapi.service.RegisterService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RegisterController {
	
	@Autowired
	private RegisterService service;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerClient(@RequestBody ClientRequest client , HttpServletRequest request) throws Exception{
		
		String url = request.getRequestURL().toString();
		url = url.replace(request.getServletPath() ,  "");

		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(service.registerclient(client ,url));
					
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyClient(@Param("code") String code){
		
		String verified = service.verify(code) ? "Verify successfully" : "please register";
		HttpStatus status = service.verify(code) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		
		return ResponseEntity
							.status(status)
							.body(verified);
	}

}
