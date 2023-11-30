package com.callthirdpartyapi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.callthirdpartyapi.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

	Optional<Client> findByUsername(String username);
}
