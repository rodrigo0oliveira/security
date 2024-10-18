package br.com.api.projeto.services;

import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.dto.RoomDto;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.RoomRepository;
import br.com.api.projeto.model.services.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    Room room;

    @InjectMocks
    @Spy
    RoomService roomService;

    @Mock
    RoomRepository roomRepository;

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
    void testCreateRoomShouldBeSucess(){

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomDto roomDto = new RoomDto("2",new BigDecimal("30"),RoomType.SOLTEIRO);


        String message = roomService.createRoom(roomDto);
        String expected = "Quarto criado";

        Assertions.assertEquals(message, expected);

        verify(roomRepository).save(any(Room.class));

    }

    @Test
    void testEditRoomShouldReturnRoomNotFound(){

       when(roomRepository.findByroomnumber(any(String.class))).thenReturn(null);

       String actualMessage = roomService.editRoom("1",room);
       String expectedMessage = "Quarto não encontrado";

       Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testEditRoomShouldReturnRoomEdited(){

        when(roomRepository.findByroomnumber(any(String.class))).thenReturn(room);
        doNothing().when(roomService).editRoomInformation(room,room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        String actualMessage = roomService.editRoom("1",room);
        String expectedMessage = "Quarto atualizado";

        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    void testFindRoomsToAuthenticateShouldReturnDtoList(){

        when(roomRepository.findAll()).thenReturn(Collections.singletonList(room));

        RoomDto expected = new RoomDto(room.getRoomnumber(),room.getDailyPrice(),room.getRoomtype());

        List<RoomDto> list = roomService.findAllRoomToUserAuthenticate();
        RoomDto actual = list.get(0);


        Assertions.assertEquals(actual, expected);

    }


}
