package com.callthirdpartyapi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.callthirdpartyapi.model.Client;

import jakarta.transaction.Transactional;

public interface ClientRepository extends JpaRepository<Client, Long>{

	public Optional<Client> findByUsername(String username);
	
	public Client findByVerificationCode(String code);
	
	@Query(value = "UPDATE client SET failed_attempt = :attempt WHERE username = :username" , nativeQuery = true)
	//@Modifying
	@Modifying
	@Transactional
	public int updateFailedAttempt(@Param("attempt") Short attempt, @Param("username") String username);
	
}
