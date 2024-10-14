package br.com.api.projeto.repository;

import br.com.api.projeto.model.domain.Reserve;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.ReserveRepository;
import br.com.api.projeto.model.repository.RoomRepository;
import br.com.api.projeto.model.repository.UserRepository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
public class ReserveRepositoryTest {

    ReserveRepository reserveRepository;

    RoomRepository roomRepository;

    UserRepository userRepository;

    Room firstRoom;
    Room secondRoom;

    User firstUser;
    User secondUser;

    @Autowired
    ReserveRepositoryTest(ReserveRepository reserveRepository,RoomRepository roomRepository
            ,UserRepository userRepository){
        this.reserveRepository = reserveRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setupUser(){

        firstUser = User.builder()
                .email("test@gmail.com")
                .roles(null)
                .document("123456123")
                .password("123")
                .username("test")
                .build();

        secondUser = User.builder()
                .email("second@gmail.com")
                .roles(null)
                .document("123456789")
                .password("123")
                .username("second")
                .build();

        userRepository.save(firstUser);
        userRepository.save(secondUser);

    }

    @BeforeEach
    void setupRooms() {
        firstRoom = Room.builder()
                .roomtype(RoomType.SOLTEIRO)
                .roomnumber("4")
                .dailyPrice(new BigDecimal("30"))
                .status(Status.DISPONÍVEL)
                .build();

        secondRoom = Room.builder()
                .roomtype(RoomType.SOLTEIRO)
                .roomnumber("5")
                .dailyPrice(new BigDecimal("30"))
                .status(Status.DISPONÍVEL)
                .build();

        roomRepository.save(firstRoom);
        roomRepository.save(secondRoom);

    }


    @Test
    void testFindAllReservesByRoomIdShouldReturnListReserve(){

        Reserve firtsReserve = Reserve.builder()
                .room(firstRoom)
                .user(firstUser)
                .checkout(LocalDate.now())
                .checkin(LocalDate.now())
                .build();

        Reserve secondReserve = Reserve.builder()
                .room(secondRoom)
                .user(secondUser)
                .checkout(LocalDate.now())
                .checkin(LocalDate.now())
                .build();

        reserveRepository.save(firtsReserve);
        reserveRepository.save(secondReserve);
        List<Reserve> list = reserveRepository.findAllReservesByRoomId(firtsReserve.getRoom().getId());

        Assertions.assertTrue(list.contains(firtsReserve));
        Assertions.assertEquals(list.get(0), firtsReserve);
    }

    @Test
    void testFindAllReservesByUserIdShouldReturnListReserve(){
        Reserve firtsReserve = Reserve.builder()
                .room(null)
                .user(firstUser)
                .checkout(LocalDate.now())
                .checkin(LocalDate.now())
                .build();

        Reserve secondReserve = Reserve.builder()
                .room(null)
                .user(secondUser)
                .checkout(LocalDate.now())
                .checkin(LocalDate.now())
                .build();

        reserveRepository.save(firtsReserve);
        reserveRepository.save(secondReserve);

        List<Reserve> list = reserveRepository.findAllReservesByUserId(firtsReserve.getUser().getId());

        Assertions.assertTrue(list.contains(firtsReserve));
        Assertions.assertFalse(list.contains(secondReserve));

    }




}
