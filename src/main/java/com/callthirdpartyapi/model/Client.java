package com.callthirdpartyapi.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String role;
	
	private Boolean enable;
	
	private String verificationCode;
	
	private Short failedAttempt;
	
	private Boolean isAccountNonLock;
	
	private Date lockTime;
	
	private Date createdAt;

}
