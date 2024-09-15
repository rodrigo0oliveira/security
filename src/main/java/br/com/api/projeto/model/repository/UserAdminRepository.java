package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.UserAdmin;

@Repository
public  interface UserAdminRepository extends JpaRepository<UserAdmin, String>{

}
