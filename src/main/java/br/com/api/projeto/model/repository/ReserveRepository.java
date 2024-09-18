package br.com.api.projeto.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.api.projeto.model.domain.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, String>{
	
	 @Query("SELECT u FROM Reserve u WHERE u.room.id = :roomId")
	   List<Reserve> verifyDates(@Param("roomId") String roomId);

}
