package br.com.api.projeto.model.controller;

import br.com.api.projeto.model.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Login e Cadastro",description = "Controlador para criação de conta,autenticação e alteração de senha")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AuthController {
	
	private final AuthService authService;
	
	private final EmailService emailService;

	@Operation(summary = "Cadastrar um novo usuario",description = "Metodo para cadastrar um novo usuário")
	@ApiResponse(responseCode = "201",description = "Usuário criado com sucesso")
	@ApiResponse(responseCode = "409",description = "Valor informado já existe")
	@ApiResponse(responseCode = "400",description = "Algum valor informado é nulo")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@PostMapping("/signup")
	public ResponseEntity<String> createAccount(@Valid @RequestBody RegisterDto request){
		String message = authService.createAccount(request);
		emailService.sendEmail(request.getEmail(),request.getUsername());
		return new ResponseEntity<>(message,HttpStatus.CREATED);
	}

	@Operation(summary = "Realizar login",description = "Metodo para realizar login e receber token JWT")
	@ApiResponse(responseCode = "200",description = "Informações válidas")
	@ApiResponse(responseCode = "401",description = "Usuário ou senha inválido")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDto login) throws Exception{
			TokenResponse tokenResponse = authService.login(login);
			return new ResponseEntity<>(tokenResponse,(HttpStatus.OK));
	}

	@Operation(summary = "Alterar senha",description = "Metodo para alterar a senha")
	@ApiResponse(responseCode = "200",description = "Senha alterada")
	@ApiResponse(responseCode = "404",description = "Usuário não encontrado")
	@ApiResponse(responseCode = "500",description = "Erro no servidor")
	@PutMapping("/changepassword/{username}")
	public ResponseEntity<String> changePassword(@PathVariable String username){
		String message = authService.changePassword(username);
		if(message.startsWith("Usuário não encontrado")) {
			return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(message,HttpStatus.OK);
	}

}
