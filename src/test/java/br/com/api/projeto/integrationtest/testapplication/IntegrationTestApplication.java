package br.com.api.projeto.integrationtest.testapplication;

import br.com.api.projeto.config.TestConfig;
import br.com.api.projeto.integrationtest.testcontainers.AbstractIntegrationTest;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.dto.LoginDto;
import br.com.api.projeto.model.domain.dto.NewReserveDto;
import br.com.api.projeto.model.domain.dto.RegisterDto;
import br.com.api.projeto.model.domain.dto.RoomDto;
import br.com.api.projeto.model.domain.enums.RoomType;
import br.com.api.projeto.model.domain.enums.Status;
import br.com.api.projeto.model.security.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTestApplication extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;

    private static ObjectMapper objectMapper;

    private static RoomDto roomDto;

    private static TokenResponse tokenResponseAdmin;

    private static TokenResponse tokenResponseGuest;

    private static RegisterDto registerAdminDto;

    private static LoginDto loginAdminDto;

    private static RegisterDto registerGuestDto;

    private static LoginDto loginGuesDto;

    @BeforeAll
    static void setup() throws JsonProcessingException {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        requestSpecification = new RequestSpecBuilder()
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        roomDto = new RoomDto("77",new BigDecimal("90"), RoomType.SOLTEIRO);

        //ADMIN
        registerAdminDto = new RegisterDto("test1","test1@gmail.com","123","35835401078", "ADMIN");
        loginAdminDto = new LoginDto(registerAdminDto.getUsername(), registerAdminDto.getPassword());
        //GUEST
        registerGuestDto = new RegisterDto("test2","test2@gmail.com","123","22306099049","GUEST");
        loginGuesDto = new LoginDto(registerGuestDto.getUsername(),registerGuestDto.getPassword());

    }

    //ADMIN
    @Order(1)
    @Test
    void integrationTestWhenRegisterAdminShouldReturnCreated() throws JsonProcessingException {

        var contentRegister = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("/security/auth/signup")
                .body(registerAdminDto)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        Assertions.assertEquals("Conta criada",contentRegister);
    }

    @Order(2)
    @Test
    void testLoginAdminWhenInformationIsValidShouldReturnOk() throws JsonProcessingException {
        var contentLogin = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/auth/login")
                .body(loginAdminDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        this.tokenResponseAdmin = objectMapper.readValue(contentLogin,TokenResponse.class);
        Assertions.assertEquals(tokenResponseAdmin.getUsername(), loginAdminDto.getUsername());
        Assertions.assertNotNull(tokenResponseAdmin);
    }

    @Order(3)
    @Test
    void testCreateTwoRoomsShouldReturnCreated(){
        var contentCreateRoom = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/room/create")
                .header("Authorization","Bearer "+ tokenResponseAdmin.getToken())
                .body(roomDto)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().body().asString();

        Assertions.assertEquals("Quarto criado",contentCreateRoom);

        var contentCreateSecondRoom = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/room/create")
                .header("Authorization","Bearer "+ tokenResponseAdmin.getToken())
                .body(new RoomDto("92",roomDto.getDailyPrice(),roomDto.getRoomType()))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().body().asString();

        Assertions.assertEquals("Quarto criado",contentCreateSecondRoom);
    }

    @Order(4)
    @Test
    void testEditRoomWhenRoomExistShouldReturnOk(){
        var contentEditRoom = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/room/edit/{roomnumber}")
                .pathParam("roomnumber",roomDto.getRoomnumber())
                .body(new Room(RoomType.SUÍTE,new BigDecimal("190"),Status.DISPONÍVEL))
                .header("Authorization","Bearer "+ tokenResponseAdmin.getToken())
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Assertions.assertEquals("Quarto atualizado",contentEditRoom);
    }

    @Order(5)
    @Test
    void testEditRoomWhenRoomNotExistShouldReturnNotFound(){
        var contentEditRoomFalse = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/room/edit/{roomnumber}")
                .pathParam("roomnumber","990")
                .body(new Room(RoomType.SUÍTE,new BigDecimal("190"),Status.DISPONÍVEL))
                .header("Authorization","Bearer "+ tokenResponseAdmin.getToken())
                .when()
                .put()
                .then()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        Assertions.assertEquals("Quarto não encontrado",contentEditRoomFalse);
    }

    @Order(6)
    @Test
    void testFindAllShouldReturnOk() throws JsonProcessingException {
        var contentFindAll = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/room/findAll")
                .header("Authorization","Bearer "+ tokenResponseAdmin.getToken())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        Assertions.assertTrue(contentFindAll.contains(roomDto.getRoomnumber()));
        Assertions.assertNotNull(contentFindAll);
    }

    //GUEST
    @Order(7)
    @Test
    void testCreateRegisterGuestShouldReturnCreated(){
        var contentRegister = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("/security/auth/signup")
                .body(registerGuestDto)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        Assertions.assertEquals("Conta criada",contentRegister);
    }

    @Order(8)
    @Test
    void testLoginGuestWhenInformationIsValidShouldReturnOk() throws JsonProcessingException {
        var contentLogin = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/auth/login")
                .body(loginGuesDto)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        this.tokenResponseGuest = objectMapper.readValue(contentLogin,TokenResponse.class);
        Assertions.assertEquals(tokenResponseGuest.getUsername(), loginGuesDto.getUsername());
        Assertions.assertNotNull(tokenResponseGuest);
    }

    @Order(9)
    @Test
    void testCreateReserveShouldReturnCreated(){
        NewReserveDto reserveDto = new NewReserveDto("22-10-2024","25-10-2024",roomDto.getRoomnumber());

        var contentReserve = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/reserve/create")
                .body(reserveDto)
                .header("Authorization","Bearer "+ tokenResponseGuest.getToken())
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().body().asString();

        Assertions.assertEquals("Reserva criada",contentReserve);
    }

    @Order(10)
    @Test
    void testCreateReserveWhenDatesAreNotAvailableShouldReturnBadRequest(){
        NewReserveDto reserveDto = new NewReserveDto("22-10-2024","23-10-2024",roomDto.getRoomnumber());

        var contentReserve = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/reserve/create")
                .body(reserveDto)
                .header("Authorization","Bearer "+ tokenResponseGuest.getToken())
                .when()
                .post()
                .then()
                .statusCode(409)
                .extract().body().asString();



    }

    @Order(11)
    @Test
    void testFindAllReservesToUserAuthenticatedShouldReturnOk(){
        var contentFindAll = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE)
                .basePath("security/reserve/findAll/me")
                .header("Authorization","Bearer "+ tokenResponseGuest.getToken())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Assertions.assertTrue(contentFindAll.contains(loginGuesDto.getUsername()));
    }
}
