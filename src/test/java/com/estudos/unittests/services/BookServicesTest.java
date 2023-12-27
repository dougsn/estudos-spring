package com.estudos.unittests.services;

import com.estudos.data.dto.BookDTO;
import com.estudos.data.dto.BookDTOMapper;
import com.estudos.data.dto.BookDTOMapperList;
import com.estudos.data.model.Book;
import com.estudos.repository.BookRepository;
import com.estudos.services.exceptions.BadRequestException;
import com.estudos.services.exceptions.ObjectNotFoundException;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
    @Mock
    BookDTOMapperList listMapper;

    @BeforeEach
    void setUpMocks() throws Exception {
        // Inicializando o input e setar os mocks no BookServices
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Book> books = input.mockEntityList();
        List<BookDTO> dtoList = input.mockDTOList();
        // Quando o findAll for chamado, o mock acima será retornado.
        when(repository.findAll()).thenReturn(books);
        when(listMapper.apply(anyList())).thenReturn(dtoList);

        // Chamando o findAll para retornar o mock
        var book = service.findAll();
        assertNotNull(book);
        assertEquals(10, book.size());

        var bookOne = book.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());
        assertTrue(bookOne.toString().contains("links: [<http://localhost/api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test 1", bookOne.getAuthor());
        assertEquals("Title Test 1", bookOne.getTitle());
        assertEquals(1.0, bookOne.getPrice());
        assertEquals(LocalDate.now(), bookOne.getLaunchDate());

        var bookSeven = book.get(7);
        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());
        assertTrue(bookSeven.toString().contains("links: [<http://localhost/api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Author Test 7", bookSeven.getAuthor());
        assertEquals("Title Test 7", bookSeven.getTitle());
        assertEquals(7.0, bookSeven.getPrice());
        assertEquals(LocalDate.now(), bookSeven.getLaunchDate());
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(input.mockEntity(1)));
        when(mapper.apply(any(Book.class))).thenReturn(input.mockDTO(1));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [<http://localhost/api/book/v1/1>;rel=\"self\"]"));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(LocalDate.now(), result.getLaunchDate());
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(ObjectNotFoundException.class, () -> service.findById(null));
    }

    @Test
    void testCreate() {
        when(repository.save(any(Book.class))).thenReturn(input.mockEntity(2));
        when(mapper.apply(any(Book.class))).thenReturn(input.mockDTO(2));

        var result = service.create(input.mockDTO(2));
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [<http://localhost/api/book/v1/2>;rel=\"self\"]"));

        assertEquals("Author Test 2", result.getAuthor());
        assertEquals("Title Test 2", result.getTitle());
        assertEquals(2.0, result.getPrice());
        assertEquals(LocalDate.now(), result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook() {
        assertThrows(BadRequestException.class, () -> service.create(null));
    }

    @Test
    void testUpdate() {
        when(repository.findById(1L)).thenReturn(Optional.of(input.mockEntity(1)));
        when(repository.save(any(Book.class))).thenReturn(input.mockEntity(1));
        when(mapper.apply(any(Book.class))).thenReturn(input.mockDTO(1));

        var result = service.update(input.mockDTO(1));
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [<http://localhost/api/book/v1/1>;rel=\"self\"]"));

        assertEquals("Author Test 1", result.getAuthor());
        assertEquals("Title Test 1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(LocalDate.now(), result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook() {
        assertThrows(BadRequestException.class, () -> service.update(null));
    }

    @Test
    void testDelete() {
        Book book = input.mockEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
    }
}
