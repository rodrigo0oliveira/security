package br.com.api.projeto.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
