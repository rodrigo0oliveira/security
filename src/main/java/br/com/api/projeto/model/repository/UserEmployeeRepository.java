package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.UserEmployee;

@Repository
public interface UserEmployeeRepository extends JpaRepository<UserEmployee, String>{

}
