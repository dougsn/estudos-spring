package com.estudos.data.dto;

import com.estudos.data.model.Book;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookDTOMapper implements Function<Book, BookDTO> {

    @Override
    public BookDTO apply(Book book) {
        return new BookDTO(
                book.getId(),
                book.getAuthor(),
                book.getLaunch_date(),
                book.getPrice(),
                book.getTitle()
        );
    }
}
