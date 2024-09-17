package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.projeto.model.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, String>{
	
	

}
