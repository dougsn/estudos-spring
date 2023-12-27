package com.estudos.integrationtests.controller;

import com.estudos.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudos.integrationtests.vo.PersonVO;
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
public class PersonControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Desabilita falhas com props desconhecidos (hateoas)

        person = new PersonVO();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockPerson();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirst_name());
        assertNotNull(persistedPerson.getLast_name());
        ;
        assertNotNull(persistedPerson.getAddress());
        ;
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);


        assertEquals("Richard", persistedPerson.getFirst_name());
        assertEquals("Stallman", persistedPerson.getLast_name());
        assertEquals("New York City - New York, US", persistedPerson.getAddress());
        ;
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() {
        mockPerson();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
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
        mockPerson();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirst_name());
        assertNotNull(persistedPerson.getLast_name());
        ;
        assertNotNull(persistedPerson.getAddress());
        ;
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);


        assertEquals("Richard", persistedPerson.getFirst_name());
        assertEquals("Stallman", persistedPerson.getLast_name());
        assertEquals("New York City - New York, US", persistedPerson.getAddress());
        ;
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void findByIdWithWrongOrigin() {
        mockPerson();

        // Criando a especificação para fazer a requisição http com o header, path, porta ..
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", person.getId())
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

    private void mockPerson() {
        person.setFirst_name("Richard");
        person.setLast_name("Stallman");
        person.setAddress("New York City - New York, US");
        person.setGender("Male");
    }

}
