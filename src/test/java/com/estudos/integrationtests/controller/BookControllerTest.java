package com.estudos.integrationtests.controller;

import com.estudos.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudos.integrationtests.vo.AuthenticationRequest;
import com.estudos.integrationtests.vo.AuthenticationResponse;
import com.estudos.integrationtests.vo.BookDTO;
import com.estudos.integrationtests.wrappers.WrapperBookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfigs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;
    private static AuthenticationResponse authenticationResponse;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
        authenticationResponse = new AuthenticationResponse();
    }


    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException {
        AuthenticationRequest user = new AuthenticationRequest("Administrator", "admin123");

        var token = given()
                .basePath("/api/auth/v1/login")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        authenticationResponse = objectMapper.readValue(token, AuthenticationResponse.class);
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_AUTHORIZATION, "Bearer " + authenticationResponse.getToken())
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockBook();
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        BookDTO persistedBook = objectMapper.readValue(content, BookDTO.class);
        book = persistedBook;

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunch_date());

        assertTrue(persistedBook.getId() > 0);


        assertEquals("Douglas", persistedBook.getAuthor());
        assertEquals("Nascimento", persistedBook.getTitle());
        assertEquals(30.0, persistedBook.getPrice());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        book.setAuthor("Douglas Modificado");
        book.setTitle("Nascimento Modificado");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO persistedBook = objectMapper.readValue(content, BookDTO.class);
        book = persistedBook;

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunch_date());

        assertTrue(persistedBook.getId() > 0);


        assertEquals("Douglas Modificado", persistedBook.getAuthor());
        assertEquals("Nascimento Modificado", persistedBook.getTitle());
        assertEquals(30.0, persistedBook.getPrice());
    }

    @Test
    @Order(3)
    public void findById() throws JsonProcessingException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .pathParams("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO persistedBook = objectMapper.readValue(content, BookDTO.class);
        book = persistedBook;

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunch_date());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Douglas Modificado", persistedBook.getAuthor());
        assertEquals("Nascimento Modificado", persistedBook.getTitle());
        assertEquals(30.0, persistedBook.getPrice());
        assertEquals("20/01/2023", persistedBook.getLaunch_date());
    }

    @Test
    @Order(4)
    public void testDelete() {
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        var books = wrapper.getEmbedded().getBooks();

        BookDTO bookOne = books.get(0);

        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getPrice());
        assertNotNull(bookOne.getTitle());
        assertNotNull(bookOne.getLaunch_date());

        assertEquals(15, bookOne.getId());
        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", bookOne.getAuthor());
        assertEquals("07/11/2017", bookOne.getLaunch_date());
        assertEquals(54.0, bookOne.getPrice());
        assertEquals("Implantando a governança de TI", bookOne.getTitle());
    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockBook() {
        book.setAuthor("Douglas");
        book.setTitle("Nascimento");
        book.setPrice(30.0);
        book.setLaunch_date("20/01/2023");
    }

}
