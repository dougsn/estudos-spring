package com.estudos.services.v1;

import com.estudos.controller.v1.BookController;
import com.estudos.data.dto.BookDTO;
import com.estudos.data.dto.BookDTOMapper;
import com.estudos.data.mapper.DozerMapper;
import com.estudos.data.model.Book;
import com.estudos.data.v1.BookVO;
import com.estudos.repository.BookRepository;
import com.estudos.services.exceptions.BadRequestException;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());
    @Autowired
    BookRepository repository;
    @Autowired
    BookDTOMapper mapper;

    public List<BookDTO> findAll() {
        logger.info("Finding all books!");
        var books = repository.findAll()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
        books.forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));

        return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one book.");
        var book = repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Book id: " + id + " not found"));
        book.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return book;
    }

    public BookDTO create(BookDTO book) {
        logger.info("Creating one book.");
        if (book == null) throw new BadRequestException("The book cannot be null.");

        Book newBook = repository.save(new Book(null, book.getAuthor(), book.getLaunchDate(), book.getPrice(),
                book.getTitle()));

        return mapper.apply(repository.save(newBook))
                .add(linkTo(methodOn(BookController.class).findById(newBook.getId())).withSelfRel());
    }

    public BookDTO update(BookDTO book) {
        logger.info("Creating one book.");
        if (book == null) throw new BadRequestException("The book cannot be null.");

        Book updatedBook = repository.findById(book.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Book id: " + book.getId() + " not found."));

        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setLaunch_date(book.getLaunchDate());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setTitle(book.getTitle());

        return mapper.apply(repository.save(updatedBook))
                .add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel());
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Book id: " + id + " not found."));

        repository.delete(entity);
    }


}






























