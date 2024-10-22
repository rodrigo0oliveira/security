package br.com.api.projeto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.api.projeto.model.domain.Reserve;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, String>{
	
	 @Query("SELECT u FROM Reserve u WHERE u.room.id = :roomId")
	  List<Reserve> findAllReservesByRoomId(@Param("roomId") String roomId);
	 
	 @Query("select u from Reserve u where u.user.id = :userId")
	 List<Reserve> findAllReservesByUserId(@Param("userId") String id);
}
