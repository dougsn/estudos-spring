package com.estudos.integrationtests.controller;

import com.estudos.data.dto.BookDTOMapper;
import com.estudos.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudos.integrationtests.vo.BookDTO;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockBook();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
    public void testCreateWithWrongOrigin() {
        mockBook();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void findById() throws JsonProcessingException {
        mockBook();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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

        assertEquals("Douglas", persistedBook.getAuthor());
        assertEquals("Nascimento", persistedBook.getTitle());
        assertEquals(30.0, persistedBook.getPrice());
        assertEquals("20/01/2023", persistedBook.getLaunch_date());
    }

    @Test
    @Order(4)
    public void findByIdWithWrongOrigin() {
        mockBook();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();


        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockBook() {
        book.setAuthor("Douglas");
        book.setTitle("Nascimento");
        book.setPrice(30.0);
        book.setLaunch_date("20/01/2023");
    }

}
