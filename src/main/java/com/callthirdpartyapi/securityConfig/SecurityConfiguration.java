package com.callthirdpartyapi.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.callthirdpartyapi.filter.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private CustomeSuccesserHandler successHandler;
	
	@Autowired
	private CustomeFailerHandler failerHandler;
	
	@Autowired
	private JwtAuthFilter authFilter;
	
	@Bean
	public UserDetailsService userDetailService() {
		return new CustomeUserDetailService();
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService());
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//		http
//			.csrf().disable()
//			.authorizeHttpRequests()
//			.requestMatchers( HttpMethod.POST,"/posts/authenticate/**").permitAll()
//			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.authenticationProvider(daoAuthenticationProvider())
//			.addFilterBefore(null, UsernamePasswordAuthenticationFilter.class)
//			.addFilter(authFilter)
//			.failureHandler(failerHandler)
//			.successHandler(handler)
//			.and()
//			.logout();
        
        http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(request ->
                       request.requestMatchers("/posts/authenticate").permitAll().anyRequest().authenticated())
                .sessionManagement(
                		session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                		)
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(authFilter , UsernamePasswordAuthenticationFilter.class);	
		return http.build();
	}

}
