package br.com.api.projeto.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	Optional<User> findByusername(String userName);
	
	User findBydocument(String document);
	
	User findByemail(String email);

}
