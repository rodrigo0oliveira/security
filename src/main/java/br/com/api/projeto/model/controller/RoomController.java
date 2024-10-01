package br.com.api.projeto.model.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.dto.RoomDto;
import br.com.api.projeto.model.services.RoomService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/security/room")
@RequiredArgsConstructor
@RestController
public class RoomController {
	
	private final RoomService roomService;
	
	@PostMapping("/create")
	public ResponseEntity<String> createRoom(@RequestBody Room room){
		roomService.createRoom(room);
		return new ResponseEntity<>("Quarto criado",HttpStatus.CREATED);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		List<Room> list = roomService.findAll();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhum quarto cadastrado",HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(list);
	}
	
	@PutMapping("/edit/{roomnumber}")
	public ResponseEntity<String> editRoom(@PathVariable String roomnumber,@RequestBody Room room){
		String message = roomService.editRoom(roomnumber,room);
		return new ResponseEntity<>(message,HttpStatus.OK);
		
	}
	
	@GetMapping("/findAll/auth")
	public ResponseEntity<?> findAllRoomsToUser(){
		List<RoomDto> list = roomService.findAllRoomToUserAuthenticate();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhum quarto cadastrado",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

}
