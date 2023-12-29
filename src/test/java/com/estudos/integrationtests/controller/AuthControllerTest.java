package com.estudos.integrationtests.controller;

import com.estudos.data.dto.security.RegisterRequest;
import com.estudos.integrationtests.testcontainers.AbstractIntegrationTest;
import com.estudos.integrationtests.vo.AuthenticationRequest;
import com.estudos.integrationtests.vo.AuthenticationResponse;
import com.estudos.integrationtests.vo.UserDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static UserDTO user;
    private static AuthenticationResponse authenticationResponse;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        user = new UserDTO();
        authenticationResponse = new AuthenticationResponse();
    }

    @Test
    @Order(0)
    public void testRegister() throws JsonProcessingException {
        RegisterRequest newUser = new RegisterRequest("Teste", "admin123");

        var token = given()
                .basePath("/api/auth/v1/register")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(newUser)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        authenticationResponse = objectMapper.readValue(token, AuthenticationResponse.class);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARM_AUTHORIZATION, "Bearer " + authenticationResponse.getToken())
                .setBasePath("/api/auth/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(token);
    }

    @Test
    @Order(1)
    public void testLogin() {
        AuthenticationRequest user = new AuthenticationRequest("Teste", "admin123");

        var token = given().spec(specification)
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

        assertNotNull(token);
    }



    @Test
    @Order(1)
    public void testInfoUser() throws JsonProcessingException {
        var userInfo = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        UserDTO user = objectMapper.readValue(userInfo, UserDTO.class);
        assertNotNull(user.getUsername());
    }



}
