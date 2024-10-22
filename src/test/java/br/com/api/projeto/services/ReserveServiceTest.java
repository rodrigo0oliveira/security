package br.com.api.projeto.services;

import br.com.api.projeto.model.domain.Reserve;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.dto.NewReserveDto;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.repository.ReserveRepository;
import br.com.api.projeto.model.repository.RoomRepository;
import br.com.api.projeto.model.services.AuthService;
import br.com.api.projeto.model.services.ReserveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveServiceTest {

    @InjectMocks
    @Spy
    ReserveService reserveService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    AuthService authService;

    @Mock
    Authentication authentication;

    @Mock
    ReserveRepository reserveRepository;

    User user;

    Room room;

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    public void setup(){
        user = User.builder().id(UUID.randomUUID().toString())
                .roles(null)
                .password("123")
                .document("12345678911")
                .email("teste@gmail.com")
                .username("teste")
                .build();

        room = Room.builder()
                .status(Status.DISPONÍVEL)
                .roomtype(RoomType.SOLTEIRO)
                .roomnumber("2")
                .dailyPrice(new BigDecimal("200"))
                .id(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void setupMockAuthorization(){
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateReserveShouldReturnRoomNotFound() {

        setupMockAuthorization();

        when(authService.getUser(authentication)).thenReturn(user);
        when(roomRepository.findByroomnumber(any(String.class))).thenReturn(null);


        NewReserveDto newReserveDto = new NewReserveDto("11-10-2020", "12-11-2020", "11");

        String message = reserveService.createReserve(newReserveDto);
        String expected = "Quarto não encontrado";

        Assertions.assertEquals(message, expected);
    }

    @Test
    void testCreateReserveShouldReturnReserveCreated(){

        String checkin = "10-11-2024";
        String checkout = "14-11-2024";

        Reserve reserve = Reserve.builder()
                .user(user)
                .id(UUID.randomUUID().toString())
                .checkin(LocalDate.parse(checkin,df))
                .checkout(LocalDate.parse(checkout,df))
                .room(room)
                .build();

        NewReserveDto newReserveDto = new NewReserveDto(checkin,checkout,room.getRoomnumber());



        setupMockAuthorization();
        when(authService.getUser(authentication)).thenReturn(user);
        when(roomRepository.findByroomnumber(any(String.class))).thenReturn(room);
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(reserveRepository.save(any(Reserve.class))).thenReturn(reserve);
        String expectedMessage = "Reserva criada";
        String actualMessage = reserveService.createReserve(newReserveDto);

        verify(reserveService).verifyDates(LocalDate.parse(checkin,df),LocalDate.parse(checkout,df),room.getId());
        verify(roomRepository).save(any(Room.class));
        verify(reserveRepository).save(any(Reserve.class));


        Assertions.assertEquals(expectedMessage,actualMessage);

    }
    @Test
    void testVerifyDatesShouldThrowRunTimeExceptionWhenDateAlreadyInReserve(){

        LocalDate checkin = LocalDate.parse("10-11-2020",df);
        LocalDate checkout = LocalDate.parse("11-11-2020",df);

        Reserve reserve = Reserve.builder().id(UUID.randomUUID().toString())
                        .room(null)
                            .checkin(checkin)
                                .checkout(checkout)
                                 .user(user)
                                         .build();

        when(reserveRepository.findAllReservesByRoomId("1")).thenReturn(Collections.singletonList(reserve));
        RuntimeException exception = assertThrows(RuntimeException.class,()-> {
            reserveService.verifyDates(checkin,checkout,"1");
        });

        String expectedExceptionMessage = "Esse intervalo de  data selecionada não está disponível para reserva - "+checkin+ " - "+ checkout;

        Assertions.assertEquals(exception.getMessage(), expectedExceptionMessage);
    }

    @Test
    void testVerifyDatesShouldThrowRunTimeExceptionWhenCheckoutIsBeforeCheckin(){

        String checkin = "10-11-2020";
        String checkout = "09-11-2020";

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Reserve reserve = Reserve.builder().id(UUID.randomUUID().toString())
                .room(null)
                .checkin(LocalDate.parse(checkin,df))
                .checkout(LocalDate.parse(checkout,df))
                .user(user)
                .build();

        when(reserveRepository.findAllReservesByRoomId("1")).thenReturn(Collections.singletonList(reserve));
        RuntimeException exception = assertThrows(RuntimeException.class,()-> {
            reserveService.verifyDates(LocalDate.parse(checkin,df),LocalDate.parse(checkout,df),"1");
        });

        String expectedExceptionMessage = "A data de entrada não pode ser após a data de saída";

        Assertions.assertEquals(expectedExceptionMessage,exception.getMessage());

    }
}
