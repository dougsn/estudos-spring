package com.estudos.unittests.services;

import com.estudos.data.model.Person;
import com.estudos.repository.PersonRepository;
import com.estudos.services.v1.PersonServices;
import com.estudos.unittests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        // Inicializando o input e setar os mocks no personServices
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindById() {
        Person person = input.mockEntity(1); // Mocando o ID como 1 nessa entidade.
        // Quando eu chamar o repository com ID 1, ele me retornará o person mockado acima.
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }



}
