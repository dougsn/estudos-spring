package com.estudos.integrationtests.wrappers;

import com.estudos.integrationtests.vo.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedVO implements Serializable {

    @JsonProperty("bookDTOList")
    private List<BookDTO> books;

    public BookEmbeddedVO() {
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
