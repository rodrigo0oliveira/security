package br.com.api.projeto.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.security.TokenResponse;
import br.com.api.projeto.model.services.AuthService;
import br.com.api.projeto.model.services.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/security/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final EmailService emailService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> createAccount(@Valid @RequestBody RegisterDto request){
		String message = authService.createAccount(request);
		emailService.sendEmail(request.getEmail(),request.getUsername());
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDto login) throws Exception{
			TokenResponse tokenResponse = authService.login(login);
			return new ResponseEntity<>(tokenResponse,(HttpStatus.CREATED));
	}
	
	@PutMapping("/changepassword/{username}")
	public ResponseEntity<String> changePassword(@PathVariable String username){
		String message = authService.changePassword(username);
		if(message.startsWith("Usuário não encontrado")) {
			return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(message,HttpStatus.OK);
	}

}
