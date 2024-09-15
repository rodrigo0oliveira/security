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
		Roles role = IrolesRepository.findByName(name).orElse(null);
		if(Objects.isNull(role)) {
			return IrolesRepository.save(Roles.builder()
                    .id(UUID.randomUUID().toString())
                    .name("ROLE_" + name)
            .build());
					
		}
		return role;
	}

}
