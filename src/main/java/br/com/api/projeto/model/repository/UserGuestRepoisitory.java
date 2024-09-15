package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.UserGuest;

@Repository
public interface UserGuestRepoisitory extends JpaRepository<UserGuest, String>{

}
