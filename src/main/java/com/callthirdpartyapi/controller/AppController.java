package com.callthirdpartyapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.callthirdpartyapi.dto.AuthRequest;
import com.callthirdpartyapi.dto.PostDto;
import com.callthirdpartyapi.exception.UsernameNotFoundException;
import com.callthirdpartyapi.service.AppService;
import com.callthirdpartyapi.service.JwtService;

@RestController
@RequestMapping("/posts")
public class AppController {
	
	@Autowired
	private AppService service;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping()
	public ResponseEntity<?> getPost(){
		return new ResponseEntity<>(service.getPost() , HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPostById(@PathVariable Integer id){
		return new ResponseEntity<>(service.getPostById(id), HttpStatus.ACCEPTED); 
	}
	
	@PostMapping("")
	public ResponseEntity<PostDto> addPost(@RequestBody PostDto post){
		return new ResponseEntity<>(service.addPost(post) , HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@RequestBody PostDto post ,@PathVariable Integer id){
		return new ResponseEntity<>(service.updatePost(post, id) , HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable Integer id){
		return new ResponseEntity<>(service.deletePost(id) , HttpStatus.OK);
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest){
		

	Authentication authentication	= authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
				);
	if(authentication.isAuthenticated()) {
		
		return ResponseEntity
				.ok()
				.body(jwtService.generateToken(authRequest.getUsername()));
	}
	
	else {
		throw new UsernameNotFoundException("User not exist!!");
	}
	
	}

}
