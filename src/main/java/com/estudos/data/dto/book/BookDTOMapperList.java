package com.estudos.data.dto.book;

import com.estudos.data.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class BookDTOMapperList implements Function<Page<Book>, List<BookDTO>> {

    @Override
    public List<BookDTO> apply(Page<Book> books) {
        List<BookDTO> bookList = new ArrayList<>();
        books.forEach(book -> {
            BookDTO bookDTO = new BookDTO(book.getId(), book.getAuthor(), book.getLaunch_date(),
                    book.getPrice(), book.getTitle());
            bookList.add(bookDTO);
        });
        return bookList;
    }
}
