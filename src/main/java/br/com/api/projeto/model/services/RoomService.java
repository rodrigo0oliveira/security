package br.com.api.projeto.model.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	
	private final RoomRepository roomRepository;
	
	public void createRoom(Room room) {
		Room roomBuilder = Room.builder()
				.id(UUID.randomUUID().toString())
				.roomNumber(room.getRoomNumber())
				.dailyPrice(room.getDailyPrice())
				.status(Status.DISPONÍVEL)
				.build();
		
		roomRepository.save(roomBuilder);
				
	}

}
