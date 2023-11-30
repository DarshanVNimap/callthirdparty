package com.callthirdpartyapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.callthirdpartyapi.dto.ClientRequest;
import com.callthirdpartyapi.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerClient(@RequestBody ClientRequest client){
		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(service.registerUser(client));
					
	}

//	public ResponseEntity<?> addUser(){
//		return ResponseEntity
//							.status(HttpStatus.CREATED)
//							.body();
//	}
}
