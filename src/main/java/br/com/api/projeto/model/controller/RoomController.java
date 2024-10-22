package br.com.api.projeto.model.controller;

import java.util.List;

import br.com.api.projeto.model.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Quarto",description = "Controlador para criação,busca e edição de quartos")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class RoomController {
	
	private final RoomService roomService;

	@Operation(summary = "Criar um novo quarto",description = "Metodo para criar um novo quarto")
	@ApiResponse(responseCode = "201",description = "Quarto criado com sucesso")
	@ApiResponse(responseCode = "400",description = "Algum valor informado é nulo")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@PostMapping("/create")
	public ResponseEntity<String> createRoom(@RequestBody RoomDto room){
		String message = roomService.createRoom(room);
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}

	@Operation(summary = "Buscar todos os quartos",description = "Metodo para buscar todos os quartos")
	@ApiResponse(responseCode = "200",description = "Busca concluída com sucesso")
	@ApiResponse(responseCode = "404",description = "Não existe nenhum quarto cadastrado")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		List<Room> list = roomService.findAll();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhum quarto cadastrado",HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(list);
	}

	@Operation(summary = "Editar quarto pelo número",description = "Metodo para editar um quarto pelo número")
	@ApiResponse(responseCode = "200",description = "Quarto editado com sucesso")
	@ApiResponse(responseCode = "404",description = "Não existe nenhuma quarto cadastrado com esse número")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@PutMapping("/edit/{roomnumber}")
	public ResponseEntity<String> editRoom(@PathVariable String roomnumber,@RequestBody Room room){
		String message = roomService.editRoom(roomnumber,room);
		if(message=="Quarto não encontrado"){
			return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(message,HttpStatus.OK);
		
	}

	@Operation(summary = "Buscar todos os quartos para cliente",description = "Metodo para buscar todos os quartos para o cliente")
	@ApiResponse(responseCode = "200",description = "Busca concluída com sucesso")
	@ApiResponse(responseCode = "404",description = "Não existe nenhuma quarto cadastrado")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@GetMapping("/findAll/auth")
	public ResponseEntity<?> findAllRoomsToUser(){
		List<RoomDto> list = roomService.findAllRoomToUserAuthenticate();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhum quarto cadastrado",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

}
