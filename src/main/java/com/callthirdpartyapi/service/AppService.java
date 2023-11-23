package com.callthirdpartyapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.callthirdpartyapi.dto.PostDto;

@Service
public class AppService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${base.url}")
	private String URL;
	
	private HttpHeaders gethttpHeaders() {
		
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		header.setContentType(MediaType.APPLICATION_JSON);
		
		return header;
	}
	
	public List<Map<String , Object>> getPost(){
		HttpEntity<?> entity = new HttpEntity<>(gethttpHeaders());
		ResponseEntity<?> response=restTemplate.
												exchange(
														URL, 
														HttpMethod.GET, 
														entity ,
														List.class 
														); 
		
		return (List<Map<String, Object>>) response.getBody();
	}
	
	public PostDto getPostById(Integer id) {
		HttpEntity<?> entity = new HttpEntity<>(gethttpHeaders());
		
		return restTemplate.exchange(URL.concat("/"+id), HttpMethod.GET, entity , PostDto.class).getBody();
	}
	
	public PostDto addPost(PostDto post){

		HttpEntity<?> entity = new HttpEntity<>(post , gethttpHeaders());
		return restTemplate.exchange( URL, HttpMethod.POST , entity , PostDto.class ).getBody(); 
		
	}
	
	public PostDto updatePost(PostDto post , Integer id) {
		HttpEntity<?> entity = new HttpEntity<>(post , gethttpHeaders());
		return restTemplate.exchange(URL.concat("/"+id), HttpMethod.PUT , entity , PostDto.class).getBody();
		
	}
	
	public PostDto deletePost(int id) {
		HttpEntity<?> entity = new HttpEntity<>(gethttpHeaders());
		return restTemplate.exchange(URL.concat("/"+id), HttpMethod.DELETE , entity , PostDto.class).getBody();
	}

}
