package com.estudos.unittests.services;

import com.estudos.data.dto.book.BookDTOMapper;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
