package br.com.api.projeto.model.services;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.repository.UserRepository;
import br.com.api.projeto.model.security.TokenProvider;
import br.com.api.projeto.model.security.TokenResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider provider;
	private final EmailService emailService;
	
	
	public String createAccount(RegisterDto request) {
		String password = passwordEncoder.encode(request.getPassword());
		
		User user = User.builder()
				.id(UUID.randomUUID().toString())
				.username(request.getUsername())
				.email(request.getEmail())
				.password(password)
				.document(request.getDocument())
						.roles(Collections.singletonList(roleService.getRoleByName(request.getRoleName()))).build();
						
		
		userRepository.save(user);
		
		return  "Conta criada";
	}
	


	public TokenResponse login(LoginDto login) throws Exception {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
			
			return provider.generateToken(authentication);
		}
		catch (AuthenticationException e) {
			throw new Exception("Credenciais inválidas : "+e.getMessage());
		}
		catch (Exception e) {
			throw new Exception("Erro ao autenticar: "+e.getMessage());
		}
	}
	
	
	public User getUser(Authentication authentication) {
		String id = authentication.getName();
		Optional<User> user = userRepository.findById(id);
		
		return user.orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));
		
	}
	
	@Transactional
	public String changePassword(String username) {
		Optional<User> user = userRepository.findByusername(username);
		if(user.isEmpty()) {
			return "Usuário não encontrado";
		}
		String newPassword = generateRandomPassoword();
		user.get().setPassword(newPassword);
		userRepository.save(user.get());
		String message = emailService.changePassword(user.get().getEmail(),user.get().getUsername(),newPassword);
		return "Senha alterada \n"+message;
	}
	
	private String generateRandomPassoword() {
		
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghyjklmnopqrstuvwxyz"
				+ "1234567890";
		
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0 ;i<10;i++) {
			int randomIndex = (random.nextInt(chars.length()));
			sb.append(chars.charAt(randomIndex));
		}
		
		String newPassword = sb.toString();
		
		return newPassword;
		
	}
	
	
}
