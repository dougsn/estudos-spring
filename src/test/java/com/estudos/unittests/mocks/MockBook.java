package com.estudos.unittests.mocks;

import com.estudos.data.dto.BookDTO;
import com.estudos.data.model.Book;
import com.estudos.data.model.Person;
import com.estudos.data.v1.PersonVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(Long.valueOf(number));
        book.setAuthor("Author Test " + number);
        book.setTitle("Title Test " + number);
        book.setPrice(1.0);
        book.setLaunch_date(LocalDate.now());
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setAuthor("Author Test " + number);
        book.setTitle("Title Test " + number);
        book.setPrice(1.0);
        book.setLaunchDate(LocalDate.now());
        return book;
    }

}
