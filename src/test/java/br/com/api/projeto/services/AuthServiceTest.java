package br.com.api.projeto.services;

import br.com.api.projeto.model.domain.Roles;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.repository.IRolesRepository;
import br.com.api.projeto.model.repository.UserRepository;
import br.com.api.projeto.model.security.TokenProvider;
import br.com.api.projeto.model.security.TokenResponse;
import br.com.api.projeto.model.services.AuthService;
import br.com.api.projeto.model.services.EmailService;
import br.com.api.projeto.model.services.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    @Spy
    AuthService authService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleService roleService;

    @Mock
    UserRepository userRepository;

    @Mock
    IRolesRepository iRolesRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    EmailService emailService;

    User user;

    @BeforeEach
    void setup(){

        when(roleService.getRoleByName(any(String.class))).thenReturn(new Roles("ROLE_ADMIN"));


        user = User.builder()
                .id(UUID.randomUUID().toString())
                .roles(Collections.singletonList(roleService.getRoleByName("ADMIN")))
                .username("teste")
                .password("123")
                .document("12312312311")
                .email("teste@gmail.com")
                .build();

    }

    @Test
    void setupMockAuthorization(){
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateAcccountShouldReturnSuccess(){

        RegisterDto registerDto = new RegisterDto(user.getUsername(),user.getEmail(),user.getPassword()
        ,user.getDocument(), user.getRoles().toString());


        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        String actualMessage = authService.createAccount(registerDto);
        String expectedMessage = "Conta criada";

        Assert.assertEquals(actualMessage,expectedMessage);

    }

    @Test
    void testLoginWhenCredentialsIsValid() throws Exception {
        LoginDto loginDto = new LoginDto(user.getUsername(),user.getPassword());
        TokenResponse tokenResponse = new TokenResponse("mock-token",6000,loginDto.getUsername());

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto
                .getUsername(),loginDto.getPassword()))).thenReturn(authentication);

        when(tokenProvider.generateToken(authentication)).thenReturn(tokenResponse);

        TokenResponse actualToken = authService.login(loginDto);

        Assert.assertEquals(tokenResponse,actualToken);
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(tokenProvider).generateToken(any(Authentication.class));

    }

    @Test
    void testLoginWhenCredentialsIsNotValid() throws JsonProcessingException {
        LoginDto loginDto = new LoginDto(null,"133");
        TokenResponse tokenResponse = new TokenResponse("mock-token",6000,loginDto.getUsername());

        lenient().when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto
                .getPassword()))).thenReturn(authentication);

        when(tokenProvider.generateToken(authentication)).thenThrow(new AuthenticationException("Credenciais inválidas") {
        });

        AuthenticationException authenticationException = Assert.assertThrows(AuthenticationException.class,
                ()->tokenProvider.generateToken(authentication));

        String expectedMessage = "Credenciais inválidas";

        Assert.assertEquals(expectedMessage,authenticationException.getMessage());
    }

    @Test
    void testChangePasswordWhenUserNameIsNotValid(){
        when(userRepository.findByusername(any(String.class))).thenReturn(Optional.empty());

        String actualMessage = authService.changePassword(user.getUsername());
        String expectedMessage = "Usuário não encontrado";

        Assert.assertEquals(actualMessage,expectedMessage);
        verify(userRepository).findByusername(any(String.class));
    }

    @Test
    void testChangePasswordWhenUserNameIsValid(){
        when(userRepository.findByusername(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(authService.generateRandomPassoword()).thenReturn("123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(emailService.changePassword(user.getEmail(),user.getUsername(),"123")).thenReturn("");

        String expectedMessage = "Senha alterada \n"+"";
        String actualMessage = authService.changePassword(user.getUsername());

        Assert.assertEquals(expectedMessage,actualMessage);

    }

    @Test
    void testGenerateRandomPassword(){
        String ramdomPassword = authService.generateRandomPassoword();
        Assert.assertEquals(ramdomPassword.length(),10);
    }



}
