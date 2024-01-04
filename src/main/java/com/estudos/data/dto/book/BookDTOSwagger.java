package com.estudos.data.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.time.LocalDate;

@JsonPropertyOrder({"id", "author", "title", "price", "launch_date"})
public class BookDTOSwagger implements Serializable {
    public Long id;
    public String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("launch_date")
    public LocalDate launchDate;
    public Double price;
    public String title;

    public BookDTOSwagger() {
    }

    public BookDTOSwagger(Long id, String author, LocalDate launch_date, Double price, String title) {
        this.id = id;
        this.author = author;
        this.launchDate = launch_date;
        this.price = price;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
