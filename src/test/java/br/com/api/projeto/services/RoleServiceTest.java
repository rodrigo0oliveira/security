package br.com.api.projeto.services;

import br.com.api.projeto.model.domain.Roles;
import br.com.api.projeto.model.repository.IRolesRepository;
import br.com.api.projeto.model.services.RoleService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.relation.Role;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @InjectMocks
    RoleService roleService;

    @Mock
    IRolesRepository iRolesRepository;

    @Test
    void testGetRoleByNameWhenRoleAlreadyExistShouldReturnRole(){
        Roles roles = new Roles("ADMIN");
        when(iRolesRepository.findByName(any(String.class))).thenReturn(Optional.of(roles));

        Roles newRole = roleService.getRoleByName(any(String.class));

        Assert.assertEquals(roles.getName(),newRole.getName());
        verify(iRolesRepository).findByName(any(String.class));
        verifyNoMoreInteractions(iRolesRepository);
    }

    @Test
    void testGetRoleByNameWhenRoleIsNullShouldCreatedNewRole(){
        Roles roles = new Roles("ADMIN");

        when(iRolesRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(iRolesRepository.save(any(Roles.class))).thenReturn(new Roles(roles.getName()));

        Roles newRole = roleService.getRoleByName(any(String.class));

        Assert.assertEquals(roles.getName(),newRole.getName());
        verify(iRolesRepository).save(any(Roles.class));
        verify(iRolesRepository).findByName(any(String.class));
    }
}
