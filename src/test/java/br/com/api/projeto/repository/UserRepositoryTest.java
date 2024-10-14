package br.com.api.projeto.repository;

import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    UserRepository userRepository;

    User user;

    @Autowired
    UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setup(){
        user = User.builder()
                .roles(null)
                .password("123")
                .document("12345678911")
                .email("teste@gmail.com")
                .username("teste")
                .build();

        userRepository.save(user);
    }

    @Test
    void testFindByUserNameShouldReturnUser(){
        User getUser = userRepository.findByusername("teste").get();

        Assertions.assertNotNull(getUser);
        Assertions.assertEquals(getUser.getUsername(), user.getUsername());
    }

    @Test
    void testFindByDocumentShouldReturnUser(){
        User getUser = userRepository.findBydocument("12345678911");

        Assertions.assertNotNull(getUser);
        Assertions.assertEquals(getUser, user);
    }

    @Test
    void testFindByEmailShouldReturnUser(){
        User getUser = userRepository.findByemail("teste@gmail.com");

        Assertions.assertNotNull(getUser);
        Assertions.assertEquals(getUser, user);
    }



}
