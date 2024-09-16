package br.com.api.projeto.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.projeto.model.domain.Room;

public interface RoomRepository extends JpaRepository<Room, String>{

}
