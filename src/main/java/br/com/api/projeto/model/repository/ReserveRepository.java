package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.projeto.model.domain.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, String>{

}
