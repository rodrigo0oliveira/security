package br.com.api.projeto.model.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.Roles;
import br.com.api.projeto.model.repository.IRolesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	
	private final IRolesRepository IrolesRepository;
	
	public Roles getRoleByName(String name) {
		Roles role = IrolesRepository.findByName(name);
		if(Objects.isNull(role)) {
			Roles newRole = Roles.builder().id(UUID.randomUUID().toString()).name("ROLE_"+name).build();
			return IrolesRepository.save(newRole);
					
		}
		return role;
	}

}