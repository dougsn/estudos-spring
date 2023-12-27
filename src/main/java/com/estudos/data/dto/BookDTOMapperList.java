package com.estudos.data.dto;

import com.estudos.data.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class BookDTOMapperList implements Function<List<Book>, List<BookDTO>> {

    @Override
    public List<BookDTO> apply(List<Book> books) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        books.forEach(book -> {
            BookDTO bookDTO = new BookDTO(book.getId(), book.getAuthor(), book.getLaunch_date(),
                    book.getPrice(), book.getTitle());
            bookDTOList.add(bookDTO);
        });
        return bookDTOList;
    }
}
