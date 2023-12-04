package com.callthirdpartyapi.securityConfig;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.callthirdpartyapi.exception.UsernameNotFoundException;
import com.callthirdpartyapi.model.Client;
import com.callthirdpartyapi.repo.ClientRepository;
import com.callthirdpartyapi.service.RegisterService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomeFailerHandler extends SimpleUrlAuthenticationFailureHandler{
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private RegisterService service;
	
	@Value("${account.fail.attempt}")
	private Short TIME_OF_ATTEMPT;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		Client c = clientRepo.findByUsername(
				request.getParameter("username")
				).orElseThrow(() -> new UsernameNotFoundException("Username not register!!"));
		
		if(c.getEnable()) {
			if(c.getIsAccountNonLock()) {
				if(c.getFailedAttempt() < TIME_OF_ATTEMPT) {
					service.increaseFailAttempt(c);
				}
				else {
					service.lock(c);
					exception = new LockedException("Your account is loked try after sometime");
				}
			}
			else {
				if(service.unlockAccountTimeExpired(c)) {
					exception = new LockedException("Account is unclocked");
				}
				else {
					exception = new LockedException("Account is locked try after search");
				}
			}
		}
		else {
			exception = new LockedException("Please verify your account");
		}
		
		super.onAuthenticationFailure(request, response, exception);
	}

	
}
