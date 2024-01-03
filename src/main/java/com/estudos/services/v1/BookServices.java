package com.estudos.services.v1;

import com.estudos.controller.v1.BookController;
import com.estudos.controller.v1.PersonController;
import com.estudos.data.dto.book.BookDTO;
import com.estudos.data.dto.book.BookDTOMapper;
import com.estudos.data.dto.book.BookDTOMapperList;
import com.estudos.data.model.Book;
import com.estudos.repository.BookRepository;
import com.estudos.services.exceptions.BadRequestException;
import com.estudos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    private Logger logger = Logger.getLogger(BookServices.class.getName());
    @Autowired
    BookRepository repository;
    @Autowired
    BookDTOMapper mapper;
    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Finding all books!");
        var books = repository.findAll(pageable);

        var dtoList = books.map(b -> mapper.apply(b));
        dtoList.forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));

        Link link = linkTo(methodOn(BookController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
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

        return mapper.apply(newBook)
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






























