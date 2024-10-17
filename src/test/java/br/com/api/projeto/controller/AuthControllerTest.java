package br.com.api.projeto.controller;

import br.com.api.projeto.model.controller.AuthController;
import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.exceptions.CustomAuthenticationEntryPoint;
import br.com.api.projeto.model.security.SecurityConfiguration;
import br.com.api.projeto.model.security.TokenProvider;
import br.com.api.projeto.model.security.TokenResponse;
import br.com.api.projeto.model.services.AuthService;
import br.com.api.projeto.model.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfiguration.class, H2ConsoleProperties.class, CustomAuthenticationEntryPoint.class})
public class AuthControllerTest {

    @MockBean
    AuthService authService;

    @MockBean
    EmailService emailService;

    @MockBean
    TokenProvider tokenProvider;

    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @Autowired
    AuthControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testCreateAccountShouldReturnCreated() throws Exception {
        RegisterDto registerDto =
                new RegisterDto("test","test@gmail","123","56676552507","ADMIN");
        when(authService.createAccount(registerDto)).thenReturn("Conta criada");
        when(emailService.sendEmail(registerDto.getEmail(),registerDto.getEmail())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/security/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        response.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    void testCreateAccountWhenDocumentIsNotValidShouldReturnBadRequest() throws Exception {
        RegisterDto registerDto =
                new RegisterDto("test", "test@gmail", "123", "bad123", "ADMIN");
        when(authService.createAccount(registerDto)).thenReturn(null);
        when(emailService.sendEmail(registerDto.getEmail(), registerDto.getEmail())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/security/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccountWhenSomethingValueIsNullShouldReturnBadRequest() throws Exception {
        RegisterDto registerDto =
                new RegisterDto(null,"test@gmail","123","56676552507","ADMIN");

        when(authService.createAccount(registerDto)).thenReturn(null);
        when(emailService.sendEmail(registerDto.getEmail(), registerDto.getEmail())).thenReturn(null);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/security/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void testLoginWhenUsernamePasswordIsValidShouldReturnOk() throws Exception {
        LoginDto loginDto = new LoginDto("user","123");

        when(authService.login(loginDto)).thenReturn(new TokenResponse("mock_tocken"
                ,6000l,loginDto.getUsername()));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/security/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        response.andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void testChangePasswordWhenUserExistShouldReturnOk() throws Exception {

        when(authService.changePassword("user")).thenReturn("Senha alterada");

        ResultActions response = mockMvc.perform(put("/security/auth/changepassword/{username}","user"));

        response.andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testChangePasswordWhenUserNotExistShouldReturnNotFound() throws Exception {
        when(authService.changePassword("user")).thenReturn("Usuário não encontrado");

        ResultActions response = mockMvc.perform(put("/security/auth/changepassword/{username}","user"));

        response.andDo(print())
                .andExpect(status().isNotFound());

    }



}
