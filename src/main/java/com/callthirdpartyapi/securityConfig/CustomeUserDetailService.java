package com.callthirdpartyapi.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.callthirdpartyapi.model.Client;
import com.callthirdpartyapi.repo.ClientRepository;

@Component
public class CustomeUserDetailService implements UserDetailsService {
	
	@Autowired
	private ClientRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Client client = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Registered user") );
		return new CustomeUserDetails(client);
	}

}
