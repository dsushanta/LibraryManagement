package com.learning.webresource;

import com.learning.dto.Book;
import com.learning.dto.Copy;
import com.learning.exception.EntryNotFoundException;
import com.learning.service.BookService;
import com.learning.service.CopyService;
import com.learning.webresource.filterbeans.BookFilterBean;
import com.learning.webresource.filterbeans.CopyFilterBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static com.learning.utils.CommonUtils.*;

@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @GET
    public List<Book> getBooks(@BeanParam BookFilterBean bookBean, @Context UriInfo uriInfo) {

        List<Book> books = new BookService().getBooks(bookBean);

        for(Book book : books) {
            String bookLink = getURISelf(uriInfo, Integer.toString(book.getBookId()), this).toString();
            book.addLink(bookLink, "self");
        }

        return books;
    }

    @GET
    @Path("/{bookId}")
    public Book getBookDetails(@PathParam("bookId") int bookId, @Context UriInfo uriInfo) {

        Book book = new BookService().getBookDetails(bookId);
        String bookLink = getURISelf(uriInfo, Integer.toString(book.getBookId()), this).toString();
        book.addLink(bookLink, "self");

        return book;
    }

    @PUT
    @Path("/{bookId}")
    public Book updateBookDetails(Book book,
                                  @PathParam("bookId") int bookId,
                                  @Context UriInfo uriInfo) {

        book = new BookService().updateBookDetails(bookId, book);
        String bookLink = getURISelf(uriInfo, Integer.toString(book.getBookId()), this).toString();
        book.addLink(bookLink, "self");

        return book;
    }

    @POST
    public Response addNewBook(Book book, @Context UriInfo uriInfo) {

        Book newBook = new BookService().addNewBook(book);
        new CopyService().addNewCopy(newBook.getBookId());
        URI bookURI = getURISelf(uriInfo, Integer.toString(book.getBookId()), this);
        newBook.addLink(bookURI.toString(), "self");
        Response response = Response.created(bookURI)
                .entity(newBook)
                .build();

        return response;
    }

    @DELETE
    @Path("/{bookId}")
    public void deleteBook(@PathParam("bookId") int bookId) {

        new BookService().deleteBook(bookId);
    }

    @GET
    @Path("/{bookId}/copies")
    public List<Copy> getCopies(@BeanParam CopyFilterBean copyBean, @PathParam("bookId") int bookId, @Context UriInfo uriInfo) {

        copyBean.setBookId(bookId);
        List<Copy> copies = new CopyService().getCopies(copyBean);

        for(Copy copy : copies) {
            String copyLink = getURISelf(uriInfo, Integer.toString(copy.getCopyId()), this).toString();
            copy.addLink(copyLink, "self");
        }

        return copies;
    }

    @GET
    @Path("/{bookId}/copies/{copyId}")
    public Copy getCopyDetails(@PathParam("copyId") int copyId,
                               @PathParam("bookId") int bookId,
                               @Context UriInfo uriInfo) {

        Copy copy = new CopyService().getCopyDetails(copyId);
        if(bookId != copy.getBookId())
            throw new EntryNotFoundException("Book Id belonging to the copy id is different from the book id present in the URL");
        String copyLink = getURISelf(uriInfo, Integer.toString(copy.getCopyId()), this).toString();
        copy.addLink(copyLink, "self");

        return copy;
    }

    @DELETE
    @Path("/{bookId}/copies/{copyId}")
    public void deleteCopy(@PathParam("copyId") int copyId,
                           @PathParam("bookId") int bookId) {

        Copy copy = new CopyService().getCopyDetails(copyId);
        if(bookId != copy.getBookId())
            throw new EntryNotFoundException("Book Id belonging to the copy id is different from the book id present in the URL");
        new CopyService().deleteCopyFromDatabase(copyId);
    }

    @Path("/{bookId}/bookissue")
    public BookIssueResource getBookIssueResource() {
        return new BookIssueResource();
    }

    @Path("/genres")
    public GenreResource getGenreResource() {
        return new GenreResource();
    }

}