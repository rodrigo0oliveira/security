package br.com.api.projeto.controller;

import br.com.api.projeto.model.controller.ReserveController;
import br.com.api.projeto.model.domain.Reserve;
import br.com.api.projeto.model.domain.Roles;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.dto.NewReserveDto;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.security.SecurityConfiguration;
import br.com.api.projeto.model.security.TokenProvider;
import br.com.api.projeto.model.services.ReserveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReserveController.class)
@Import({SecurityConfiguration.class, H2ConsoleProperties.class})
public class ReserveControllerTest {

    @MockBean
    ReserveService reserveService;

    @MockBean
    TokenProvider tokenProvider;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    Room room;

    Reserve reserve;

    User user;

    @Autowired
    ReserveControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @BeforeEach
    void setup(){
        room = Room.builder().id(UUID.randomUUID().toString())
                .roomnumber("1")
                .roomtype(RoomType.SOLTEIRO)
                .dailyPrice(new BigDecimal("300"))
                .status(Status.DISPONÍVEL)
                .build();

        user = User.builder()
                .email("test@gmail.com")
                .document("56676552507")
                .password("123")
                .roles(Collections.singletonList(new Roles("ADMIN"))).build();

        reserve = Reserve.builder()
                .checkin(LocalDate.now())
                .checkout(LocalDate.now())
                .room(room)
                .user(user)
                .build();
    }

    @Test
    @WithMockUser(username = "user",authorities = {"ROLE_GUEST"})
    void testCreateReserveWhenInformationsIsValidShouldReturnCreated() throws Exception {
        NewReserveDto newReserveDto = new NewReserveDto("11-12-2024"
                ,"12-12-2024",reserve.getRoom().getRoomnumber());
        when(reserveService.createReserve(newReserveDto)).thenReturn("Reserva criada");

        ResultActions response = mockMvc.perform(post("/security/reserve/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReserveDto)));


        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user",authorities = {"ROLE_GUEST"})
    void testCreateReserveWhenSomethingInformationsIsNullShouldReturnBadRequest() throws Exception{
        NewReserveDto newReserveDto = new NewReserveDto(null
                ,"12-12-2024",reserve.getRoom().getRoomnumber());

        when(reserveService.createReserve(newReserveDto)).thenThrow(ConstraintViolationException.class);

        ResultActions response = mockMvc.perform(post("/security/reserve/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReserveDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Todos os valores precisam ser preenchidos,algum valor informado e nulo"));

    }

    @Test
    @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
    void testFindAllReservesWhenExistReservesShouldReturnOk() throws Exception {
        when(reserveService.findAllReserves()).thenReturn(Arrays.asList(reserve));

        ResultActions response = mockMvc.perform(get("/security/reserve/findAll"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(reserve))));
    }

    @Test
    @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
    void testFindAllReservesWhenNotExistReservesShouldReturnNotFound() throws Exception {
        when(reserveService.findAllReserves()).thenReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/security/reserve/findAll"));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não existe nenhuma reserva cadastrada"));

    }

    @Test
    @WithMockUser(username = "user",authorities = {"ROLE_GUEST"})
    void testFindAllReservesToUserAuthenticatedWhenUserHaveReservesShouldReturnOk() throws Exception {
        when(reserveService.findAllReservesByUser()).thenReturn(Arrays.asList(reserve));

        ResultActions response = mockMvc.perform(get("/security/reserve/findAll/me"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(reserve))));
    }

    @Test
    @WithMockUser(username = "user",authorities = "ROLE_GUEST")
    void testFindAllReservesToUserAuthenticateWhenNotExistReservesShouldReturnNotFound() throws Exception {
        when(reserveService.findAllReserves()).thenReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/security/reserve/findAll/me"));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não existe nenhuma reserva cadastrada em seu nome!"));

    }

    @Test
    @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
    void testDeleteReserveByIdWhenReserveExistShouldReturnNoContent() throws Exception {
        when(reserveService.deleteReserveById(any(String.class))).thenReturn("Reserva excluída");

        ResultActions response = mockMvc.perform(delete("/security/reserve/delete/{id}",1));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
    void testDeleteReserveByIdWhenReserveNotExistShouldReturnNotFound() throws Exception {
        when(reserveService.deleteReserveById(any(String.class))).thenReturn(null);

        ResultActions response = mockMvc.perform(delete("/security/reserve/delete/{id}",1));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }







}
