package br.com.api.projeto.model.services;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider provider;
	
	
	public void createAccount(RegisterDto request) {
		String password = passwordEncoder.encode(request.getPassword());
		
		User user = User.builder()
				.id(UUID.randomUUID().toString())
				.username(request.getUsername())
				.email(request.getEmail())
				.password(password)
				.document(request.getDocument())
						.roles(Collections.singletonList(roleService.getRoleByName(request.getRoleName()))).build();
						
		
		userRepository.save(user);		
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
	
	
}
