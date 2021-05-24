package com.bravo.johny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book extends RepresentationModel<Book> implements Serializable {

    private int bookId;
    private String title;
    private String description;
    private String author;
    private String genre;
    private int likes;
    private boolean available;
}
