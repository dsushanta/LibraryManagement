package com.bravo.johny.controller;

import com.bravo.johny.dto.Genre;
import com.bravo.johny.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


//@RestController
//@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Genre> getGenres() {

        List<Genre> genres = bookService.getAllGenre();

        return genres;
    }
}
