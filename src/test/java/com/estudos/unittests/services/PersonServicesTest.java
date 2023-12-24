package com.estudos.unittests.services;

import com.estudos.data.model.Person;
import com.estudos.data.v1.PersonVO;
import com.estudos.repository.PersonRepository;
import com.estudos.services.exceptions.BadRequestException;
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

import java.util.List;
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
    void testFindAll(){
        // Mocando a lista de Persons
        List<Person> persons = input.mockEntityList();
        // Quando o findAll for chamado, o mock acima será retornado.
        when(repository.findAll()).thenReturn(persons);
        // Chamando o findAll para retornar o mock
        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, persons.size());

        var peopleOne = people.get(1);
        assertNotNull(peopleOne);
        assertNotNull(peopleOne.getKey());
        assertNotNull(peopleOne.getLinks());
        assertTrue(peopleOne.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", peopleOne.getAddress());
        assertEquals("First Name Test1", peopleOne.getFirstName());
        assertEquals("Last Name Test1", peopleOne.getLastName());
        assertEquals("Female", peopleOne.getGender());

        var peopleFour = people.get(4);
        assertNotNull(peopleFour);
        assertNotNull(peopleFour.getKey());
        assertNotNull(peopleFour.getLinks());
        assertTrue(peopleFour.toString().contains("links: [<http://localhost/api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Addres Test4", peopleFour.getAddress());
        assertEquals("First Name Test4", peopleFour.getFirstName());
        assertEquals("Last Name Test4", peopleFour.getLastName());
        assertEquals("Male", peopleFour.getGender());

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

    @Test
    void testCreate() {
        Person entity = input.mockEntity(1);

        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        assertThrows(BadRequestException.class, () -> service.create(null));
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(1);

        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    @Test
    void testUpdateWithNullPerson() {
        assertThrows(BadRequestException.class, () -> service.update(null));
    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1); // Mocando o ID como 1 nessa entidade.
        // Quando eu chamar o repository com ID 1, ele me retornará o person mockado acima.
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);
    }


}
