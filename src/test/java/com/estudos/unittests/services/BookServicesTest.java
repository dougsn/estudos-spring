package com.estudos.unittests.services;

import com.estudos.data.dto.BookDTO;
import com.estudos.data.dto.BookDTOMapper;
import com.estudos.data.model.Book;
import com.estudos.repository.BookRepository;
import com.estudos.services.v1.BookServices;
import com.estudos.unittests.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

    MockBook input;

    @InjectMocks
    private BookServices service;

    @Mock
    BookRepository repository;
    @Mock
    BookDTOMapper mapper;

    @BeforeEach
    void setUpMocks() throws Exception {
        // Inicializando o input e setar os mocks no BookServices
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll(){
        List<Book> books = input.mockEntityList();
        // Quando o findAll for chamado, o mock acima será retornado.
        when(repository.findAll()).thenReturn(books);

        // Chamando o findAll para retornar o mock
        var book = service.findAll();
        assertNotNull(book);
        assertEquals(10, book.size());

//        var bookOne = book.get(1);
//        assertNotNull(bookOne);
//        assertNotNull(bookOne.getKey());
//        assertNotNull(bookOne.getLinks());
//        assertTrue(bookOne.toString().contains("links: [<http://localhost/api/book/v1/1>;rel=\"self\"]"));
//        assertEquals("Author Test 1", bookOne.getAuthor());
//        assertEquals("Title Test 1", bookOne.getTitle());
//        assertEquals(1.0, bookOne.getPrice());
//        assertEquals(LocalDate.now(), bookOne.getLaunchDate());
    }

    @Test
    void testFindById() {
        Book book = input.mockEntity(1); // Mocando o ID como 1 nessa entidade.
        // Quando eu chamar o repository com ID 1, ele me retornará o person mockado acima.
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.id);
        assertNotNull(result.getLinks());
//        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
//        assertEquals("Addres Test1", result.getAddress());
//        assertEquals("First Name Test1", result.getFirstName());
//        assertEquals("Last Name Test1", result.getLastName());
//        assertEquals("Female", result.getGender());
    }
//
//    @Test
//    void testCreate() {
//        Person entity = input.mockEntity(1);
//
//        Person persisted = entity;
//        persisted.setId(1L);
//
//        PersonVO vo = input.mockVO(1);
//        vo.setKey(1L);
//
//        when(repository.save(entity)).thenReturn(persisted);
//
//        var result = service.create(vo);
//        assertNotNull(result);
//        assertNotNull(result.getKey());
//        assertNotNull(result.getLinks());
//
//        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
//        assertEquals("Addres Test1", result.getAddress());
//        assertEquals("First Name Test1", result.getFirstName());
//        assertEquals("Last Name Test1", result.getLastName());
//        assertEquals("Female", result.getGender());
//    }
//
//    @Test
//    void testCreateWithNullPerson() {
//        assertThrows(BadRequestException.class, () -> service.create(null));
//    }
//
//    @Test
//    void testUpdate() {
//        Person entity = input.mockEntity(1);
//
//        Person persisted = entity;
//        persisted.setId(1L);
//
//        PersonVO vo = input.mockVO(1);
//        vo.setKey(1L);
//
//        when(repository.findById(1L)).thenReturn(Optional.of(entity));
//        when(repository.save(entity)).thenReturn(persisted);
//
//        var result = service.update(vo);
//        assertNotNull(result);
//        assertNotNull(result.getKey());
//        assertNotNull(result.getLinks());
//
//        assertTrue(result.toString().contains("links: [<http://localhost/api/person/v1/1>;rel=\"self\"]"));
//        assertEquals("Addres Test1", result.getAddress());
//        assertEquals("First Name Test1", result.getFirstName());
//        assertEquals("Last Name Test1", result.getLastName());
//        assertEquals("Female", result.getGender());
//    }
//    @Test
//    void testUpdateWithNullPerson() {
//        assertThrows(BadRequestException.class, () -> service.update(null));
//    }
//
//    @Test
//    void testDelete() {
//        Person person = input.mockEntity(1); // Mocando o ID como 1 nessa entidade.
//        // Quando eu chamar o repository com ID 1, ele me retornará o person mockado acima.
//        when(repository.findById(1L)).thenReturn(Optional.of(person));
//
//        service.delete(1L);
//    }


}
