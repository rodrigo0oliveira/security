package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.projeto.model.domain.User;

public interface UserRepository extends JpaRepository<User, String>{

}
