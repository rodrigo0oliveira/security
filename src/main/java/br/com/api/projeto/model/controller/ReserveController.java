package br.com.api.projeto.model.controller;

import java.util.List;

import br.com.api.projeto.model.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.projeto.model.domain.Reserve;
import br.com.api.projeto.model.domain.dto.NewReserveDto;
import br.com.api.projeto.model.services.ReserveService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/security/reserve")
@RequiredArgsConstructor
@Tag(name = "Reserva",description = "Controlador para criação,busca e deleção de reservas")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class ReserveController {
	
	private final ReserveService reserveService;

	@Operation(summary = "Criar uma nova reserva",description = "Metodo para criar uma nova reserva")
	@ApiResponse(responseCode = "201",description = "Reserva criada com sucesso")
	@ApiResponse(responseCode = "400",description = "Algum valor informado é nulo")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@PostMapping("/create")
	public ResponseEntity<String> createReserve (@RequestBody NewReserveDto newReserve){
		String message = reserveService.createReserve(newReserve);
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}

	@Operation(summary = "Buscar todas as reservas",description = "Metodo para buscar todas as reservas")
	@ApiResponse(responseCode = "200",description = "Busca concluída com sucesso")
	@ApiResponse(responseCode = "404",description = "Não existe nenhuma reserva cadastrada")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllReserves(){
		List<Reserve> list = reserveService.findAllReserves();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhuma reserva cadastrada",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@Operation(summary = "Buscar todas as reservas que o usuário autenticado possui",description = "Metodo para buscar todas as reservas do cliente")
	@ApiResponse(responseCode = "200",description = "Busca concluída com sucesso")
	@ApiResponse(responseCode = "404",description = "Usuário não possui nenhuma reserva cadastrada")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@GetMapping("/findAll/me")
	public ResponseEntity<?> findAllReservesByUserAuthenticated(){
		List<Reserve> list = reserveService.findAllReservesByUser();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhuma reserva cadastrada em seu nome!",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@Operation(summary = "Deletar uma reserva por id",description = "Metodo para deletar uma reserva por seu id")
	@ApiResponse(responseCode = "204",description = "Reserva deletada com sucesso")
	@ApiResponse(responseCode = "404",description = "Reserva com id informado não encontrada")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@ApiResponse(responseCode = "403",description = "Usuário não possui acesso ao recurso")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteReserveById(@PathVariable String id){
		String message = reserveService.deleteReserveById(id);
		if(message==null){
			return new ResponseEntity<>("Reserva nao encontrada",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(message,HttpStatus.NO_CONTENT);
	}

}
