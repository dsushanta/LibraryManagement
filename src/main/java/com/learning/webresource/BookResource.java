package com.learning.webresource;

import com.learning.dto.Book;
import com.learning.service.BookService;
import com.learning.service.CopyService;
import com.learning.webresource.filterbeans.BookFilterBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/books")
public class BookResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks(@BeanParam BookFilterBean bookBean, @Context UriInfo uriInfo) {

        List<Book> books = new BookService().getBooks(bookBean);

        for(Book book : books) {
            String bookLink = getURISelf(uriInfo, book).toString();
            book.addLink(bookLink, "self");
        }

        return books;
    }

    @GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookDetails(@PathParam("bookId") int bookId, @Context UriInfo uriInfo) {

        Book book = new BookService().getBookDetails(bookId);
        String bookLink = getURISelf(uriInfo, book).toString();
        book.addLink(bookLink, "self");

        return book;
    }

    @PUT
    @Path("/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book updateBookDetails(Book book,
                                  @PathParam("bookId") int bookId,
                                  @Context UriInfo uriInfo) {

        book = new BookService().updateBookDetails(bookId, book);
        String bookLink = getURISelf(uriInfo, book).toString();
        book.addLink(bookLink, "self");

        return book;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewBook(Book book, @Context UriInfo uriInfo) {

        Book newBook = new BookService().addNewBook(book);
        new CopyService().addNewCopy(newBook.getBookId());
        URI bookURI = getURISelf(uriInfo, book);
        newBook.addLink(bookURI.toString(), "self");
        Response response = Response.created(bookURI)
                .entity(newBook)
                .build();

        return response;
    }

    @DELETE
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteBook(@PathParam("bookId") int bookId) {

        new BookService().deleteBook(bookId);
    }

    @Path("/{bookId}/copies")
    public CopyResource getCopyResource() {
        return new CopyResource();
    }

    @Path("/{bookId}/bookissue")
    public BookIssueResource getBookIssueResource() {
        return new BookIssueResource();
    }

    @Path("/genres")
    public GenreResource getGenreResource() {
        return new GenreResource();
    }

    // ######################### PRIVATE METHODS #################################

    private URI getURISelf(@Context UriInfo uriInfo, Book book) {

        return uriInfo.getBaseUriBuilder()
                .path(BookResource.class)
                .path(Integer.toString(book.getBookId()))
                .build();
    }
}
