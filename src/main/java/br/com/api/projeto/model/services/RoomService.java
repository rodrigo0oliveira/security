package br.com.api.projeto.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.api.projeto.model.domain.dto.RoomEditDto;
import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.dto.RoomDto;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;

	public String createRoom(RoomDto room) {
		Room roomBuilder = Room.builder().id(UUID.randomUUID().toString()).roomnumber(room.getRoomnumber())
				.dailyPrice(room.getDailyPrice()).status(Status.DISPONÍVEL).roomtype(room.getRoomType()).build();

		roomRepository.save(roomBuilder);

		return "Quarto criado";

	}

	public List<Room> findAll() {
		List<Room> list = roomRepository.findAll();
		return list;
	}

	public String editRoom(String roomnumber, RoomEditDto room) {
		Room getRoom = roomRepository.findByroomnumber(roomnumber);
		if (getRoom==null) {
			return "Quarto não encontrado";
		}
		editRoomInformation(getRoom, room);
		roomRepository.save(getRoom);

		return "Quarto atualizado";
	}

	public void editRoomInformation(Room origin, RoomEditDto edit) {
		origin.setDailyPrice(edit.getDailyPrice());
		origin.setStatus(edit.getStatus());
		origin.setRoomtype(edit.getRoomtype());
	}

	public List<RoomDto> findAllRoomToUserAuthenticate() {
		List<Room> list = roomRepository.findAll();
		List<RoomDto> listdto = new ArrayList<RoomDto>();
		
		list.stream().forEach(l-> {
			RoomDto free = new RoomDto(l.getRoomnumber(),l.getDailyPrice(),l.getRoomtype());
			listdto.add(free);
		});
		
		return listdto;

	}

}
