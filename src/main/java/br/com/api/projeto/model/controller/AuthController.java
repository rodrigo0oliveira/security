package br.com.api.projeto.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.security.TokenResponse;
import br.com.api.projeto.model.services.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/security/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> createAccount(@RequestBody RegisterDto request){
		authService.createAccount(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginDto login) throws Exception{
			TokenResponse tokenResponse = authService.login(login);
			return new ResponseEntity<>(tokenResponse,(HttpStatus.CREATED));
	}

}
