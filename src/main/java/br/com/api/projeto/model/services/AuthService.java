package br.com.api.projeto.model.services;

import java.util.Collections;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.NewAccountRequest;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;
	
	
	public void createAccount(NewAccountRequest request) {
		User user = User.builder().id(UUID.randomUUID().toString())
				.username(request.getName())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(Collections.singleton(roleService.getRoleByName(request.getRoleName())))
				.build();
		
		userRepository.save(user);				
		
	}
	
	
}
