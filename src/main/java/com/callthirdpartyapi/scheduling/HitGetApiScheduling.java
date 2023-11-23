package com.callthirdpartyapi.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.callthirdpartyapi.service.AppService;

@Component
public class HitGetApiScheduling {
	
	@Autowired
	private AppService service;
	
	@Scheduled(fixedRate = 3000)
	public void getAllPost() {
		System.out.println(service.getPostById(1).getTitle());
		
	}

}
