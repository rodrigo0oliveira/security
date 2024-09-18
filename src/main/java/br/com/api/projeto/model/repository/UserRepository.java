package br.com.api.projeto.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.api.projeto.model.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	Optional<User> findByusername(String userName);
	
	@Query("select u from User u where u.document = ?1")
	User findByDocument(String document);

}
