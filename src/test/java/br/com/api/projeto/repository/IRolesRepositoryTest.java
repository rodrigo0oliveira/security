package br.com.api.projeto.repository;

import br.com.api.projeto.model.domain.Roles;
import br.com.api.projeto.model.repository.IRolesRepository;
import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class IRolesRepositoryTest {

    IRolesRepository iRolesRepository;

    Roles role;

    @Autowired
    IRolesRepositoryTest(IRolesRepository iRolesRepository){
        this.iRolesRepository = iRolesRepository;
    }

    @BeforeEach
    void setup(){
        role = Roles.builder()
                .name("ADMIN")
                .build();

        iRolesRepository.save(role);
    }

    @Test
    void testFindByNameShouldReturnRole(){
        Roles newRole = iRolesRepository.findByName("ADMIN").get();

        assertNotNull(newRole);
        assertEquals(role,newRole);

    }

    @Test
    void testFindByNameShouldReturnNull(){
        Optional<Roles> newRole = iRolesRepository.findByName("fail");

        assertEquals(newRole,Optional.empty());
    }
}