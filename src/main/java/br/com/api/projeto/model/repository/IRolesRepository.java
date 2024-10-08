package br.com.api.projeto.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.Roles;

@Repository
public interface IRolesRepository extends JpaRepository<Roles, String>{

	Optional<Roles> findByName(String name);

}
