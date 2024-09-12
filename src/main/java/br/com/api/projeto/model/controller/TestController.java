package br.com.api.projeto.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security/test")
public class TestController {
	
	@GetMapping("/free")
	public ResponseEntity<String> freeEndpoint(){
		return new ResponseEntity<>("Endpoint liberado",(HttpStatus.OK));
	}
	
	@GetMapping("/needAuth")
	public ResponseEntity<String> needAuthorization(){
		return new ResponseEntity<>("Autorizado",HttpStatus.OK);
	}
	
	@GetMapping("/needAdmin")
	public ResponseEntity<String> needBeAdmin(){
		return new ResponseEntity<>("Admin autorizado",HttpStatus.OK);
	}

}
