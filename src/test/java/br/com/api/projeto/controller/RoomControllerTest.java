package br.com.api.projeto.controller;

import br.com.api.projeto.model.controller.RoomController;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.dto.RoomDto;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.security.SecurityConfiguration;
import br.com.api.projeto.model.security.TokenProvider;
import br.com.api.projeto.model.services.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RoomController.class)
@WithMockUser(username = "user",authorities = {"ROLE_ADMIN"})
@Import({SecurityConfiguration.class, H2ConsoleProperties.class})
public class RoomControllerTest {

    @MockBean
    RoomService roomService;

    @MockBean
    TokenProvider tokenProvider;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    Room room;

    @Autowired
    RoomControllerTest(MockMvc mockMvc,ObjectMapper objectMapper){
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
    }

    @Test
    void testCreateRoomWhenNothingIsNullShouldReturnCreated() throws Exception {

        String expectedMessage = "Quarto criado";

        when(roomService.createRoom(any(RoomDto.class))).thenReturn(expectedMessage);

        ResultActions response = mockMvc.perform(post("/security/room/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedMessage));

    }

    @Test
    void testFindAllWhenExistRoomShouldReturnOk() throws Exception {
        List<Room> list = new ArrayList<>();
        list.add(room);

        when(roomService.findAll()).thenReturn(list);

        ResultActions response = mockMvc.perform(get("/security/room/findAll"));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testFindAllWhenNotExistRoomShouldReturnNotFound() throws Exception {
        List<Room> list = new ArrayList<>();

        String expectedMessage = "Não existe nenhum quarto cadastrado";

        when(roomService.findAll()).thenReturn(list);

        ResultActions response = mockMvc.perform(get("/security/room/findAll"));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Não existe nenhum quarto cadastrado"));


    }

    @Test
    void testEditRoomWhenRoomExistShouldReturnOk() throws Exception {
        when(roomService.editRoom("3",room)).thenReturn("Quarto atualizado");

        ResultActions response =  mockMvc.perform(put("/security/room/edit/{roomnumber}",3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)));

        response.andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void testEditRoomWhenRoomNotExistShouldReturnNotFound() throws Exception {
        String message = "Quarto não encontrado";
        when(roomService.editRoom("6",room)).thenReturn(message);

        ResultActions response = mockMvc.perform(put("/security/room/edit/{roomnumber}",6)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)));

        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void testFindAllToUserAuthenticatedWhenExistAnyRoomShouldReturnOk() throws Exception {
        when(roomService.findAllRoomToUserAuthenticate()).thenReturn(Arrays.asList(new RoomDto()));

        ResultActions response = mockMvc.perform(get("/security/room/findAll/auth"));

        response.andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    void testFindAllToUserAuthenticatedWhenNotExistAnyRoomShouldReturnNotFound() throws Exception {
        when(roomService.findAllRoomToUserAuthenticate()).thenReturn(Arrays.asList());

        ResultActions response = mockMvc.perform(get("/security/room/findAll/auth"));

        response.andDo(print())
                .andExpect(status().isNotFound());

    }








}
