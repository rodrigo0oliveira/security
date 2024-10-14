package br.com.api.projeto.repository;

import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.RoomRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
public class RoomRepositoryTest {

    RoomRepository roomRepository;

    Room room;

    @Autowired
    RoomRepositoryTest(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @BeforeEach
    void setup(){
        room = Room.builder()
                .roomtype(RoomType.SOLTEIRO)
                .roomnumber("4")
                .dailyPrice(new BigDecimal("30"))
                .status(Status.DISPONÍVEL)
                .build();

        roomRepository.save(room);
    }

    @Test
    void testFindByRoomnumberShouldReturnRoom(){
        Room actualRoom = roomRepository.findByroomnumber("4");

        Assertions.assertEquals(room.toString(), actualRoom.toString());
        Assertions.assertNotEquals(actualRoom.getId(), 0);

    }

    @Test
    void testFindByRoomNumberShouldReturnNull(){
        Room actualRoom = roomRepository.findByroomnumber("1");

        Assertions.assertNull(actualRoom);

    }

}
