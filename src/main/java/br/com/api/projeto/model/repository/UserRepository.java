package br.com.api.projeto.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.projeto.model.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	Optional<User> findByusername(String userName);

}
