package com.bravo.johny.controller;

import com.bravo.johny.controller.filterbeans.BookFilterBean;
import com.bravo.johny.dto.Book;
import com.bravo.johny.dto.Genre;
import com.bravo.johny.dto.User;
import com.bravo.johny.service.BookService;
import com.bravo.johny.service.CopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CopyService copyService;

    @GetMapping
    public List<Book> getBooks(BookFilterBean bookBean) {

        List<Book> books = bookService.getBooks(bookBean);

        for(Book book : books) {
            book.add(linkTo(methodOn(BookController.class).getBooks(bookBean)).slash(Integer.toString(book.getBookId())).withSelfRel());
        }

        return books;
    }

    @GetMapping("/{bookId}")
    public Book getBookDetails(@PathVariable("bookId") int bookId) {

        Book book = bookService.getBookDetails(bookId);
        book.add(linkTo(methodOn(BookController.class).getBookDetails(bookId)).withSelfRel());
        return book;
    }

    @PutMapping("/{bookId}")
    public Book updateBookDetails(@RequestBody Book book,
                                  @PathVariable("bookId") int bookId) {

        book = bookService.updateBookDetails(bookId, book);
        book.add(linkTo(methodOn(BookController.class).updateBookDetails(book, bookId)).withSelfRel());

        return book;
    }

    @PostMapping
    public Response addNewBook(@RequestBody Book book) {

        Book newBook = bookService.addNewBook(book);
        copyService.addNewCopy(newBook.getBookId());
        WebMvcLinkBuilder bookLink = linkTo(methodOn(BookController.class).addNewBook(book)).slash(Integer.toString(book.getBookId()));
        newBook.add(bookLink.withSelfRel());
        Response response = Response.created(bookLink.toUri())
                .entity(newBook)
                .build();

        return response;
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") int bookId) {

        bookService.deleteBook(bookId);
    }

    @GetMapping("/{bookId}/issuedUsers")
    public List<User> getIssuedUsersOfABook(@PathVariable("bookId") String bookId) {

        return bookService.getIssuedUsers(bookId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {

        List<Genre> genres = bookService.getAllGenre();
        return genres;
    }
}