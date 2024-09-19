package br.com.api.projeto.model.controller;

import java.util.List;

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
public class ReserveController {
	
	private final ReserveService reserveService;
	
	@PostMapping("/create")
	public ResponseEntity<String> createReserve (@RequestBody NewReserveDto newReserve){
		String message = reserveService.createReserve(newReserve);
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllReserves(){
		List<Reserve> list = reserveService.findAllReserves();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhuma reserva cadastrada",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("findAll/me")
	public ResponseEntity<?> findAllReservesByUserAuthenticated(){
		List<Reserve> list = reserveService.findAllReservesByUser();
		if(list.isEmpty()) {
			return new ResponseEntity<>("Não existe nenhuma reserva cadastrada em seu nome!",HttpStatus.FOUND);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> deleteReserveById(@PathVariable String id){
		String message = reserveService.deleteReserveById(id);
		return new ResponseEntity<>(message,HttpStatus.NO_CONTENT);
	}

}
