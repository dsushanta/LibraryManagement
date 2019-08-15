package com.learning.webresource;

import com.learning.dto.BookIssue;
import com.learning.dto.BookLifeCycleOperation;
import com.learning.exception.EntryNotFoundException;
import com.learning.service.BookIssueService;
import com.learning.webresource.filterbeans.BookIssueFilterBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static com.learning.utils.CommonUtils.*;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookIssueResource {

    @GET
    public List<BookIssue> getBookIssueEntries(@PathParam("bookId") int bookId,
                                               @BeanParam BookIssueFilterBean bookIssueFilterBean,
                                               @Context UriInfo uriInfo) {

        bookIssueFilterBean.setBookId(bookId);
        List<BookIssue> bookIssues = new BookIssueService().getBookIssueEntries(bookIssueFilterBean);

        for(BookIssue bookIssue : bookIssues) {
            String bookIssueLink = getURISelf(uriInfo, Integer.toString(bookIssue.getBookIssueId()), this).toString();
            bookIssue.addLink(bookIssueLink, "self");
        }

        return bookIssues;
    }

    @GET
    @Path("/{bookIssueId}")
    public BookIssue getBookIssueDetails(@PathParam("bookId") int bookId,
                                         @PathParam("bookIssueId") int bookIssueId,
                                         @Context UriInfo uriInfo) {

        BookIssue bookIssue = new BookIssueService().getBookIssueDetails(bookIssueId);
        if(bookId != bookIssue.getBookId())
            throw new EntryNotFoundException("Book Id belonging to the book issue id is different from the book id present in the URL");
        String bookIssueLink = getURISelf(uriInfo, Integer.toString(bookIssue.getBookIssueId()), this).toString();
        bookIssue.addLink(bookIssueLink, "self");

        return bookIssue;
    }

    @POST
    public Response issueACopyOfABook(@PathParam("bookId") int bookId, BookIssue bookIssue, @Context UriInfo uriInfo) {

        bookIssue.setBookId(bookId);
        bookIssue = new BookIssueService().issueABook(bookIssue);
        URI copyURI = getURISelf(uriInfo, Integer.toString(bookIssue.getBookIssueId()), this);
        bookIssue.addLink(copyURI.toString(), "self");
        Response response = Response.created(copyURI)
                .entity(bookIssue)
                .build();

        return response;
    }

    @PATCH
    @Path("/{bookIssueId}")
    public BookIssue updateBookIssueDetails(@PathParam("bookId") int bookId,
                                            @PathParam("bookIssueId") int bookIssueId,
                                            BookIssue bookIssue, @Context UriInfo uriInfo) {

        bookIssue.setBookId(bookId);
        bookIssue.setBookIssueId(bookIssueId);
        BookLifeCycleOperation operation = bookIssue.getOperation();
        if(operation.equals(BookLifeCycleOperation.REISSUE)) {
            bookIssue = new BookIssueService().reIssueABook(bookIssue);
        } else if(operation.equals(BookLifeCycleOperation.RETURN)) {
            bookIssue = new BookIssueService().returnABook(bookIssue);
        } else
            throw new EntryNotFoundException("Book Life Cycle operation : "+operation.getOperation()+" is not a valid operation");

        String copyLink = getURISelf(uriInfo, Integer.toString(bookIssue.getBookIssueId()), this).toString();
        bookIssue.addLink(copyLink, "self");

        return bookIssue;
    }

    @DELETE
    @Path("/{bookIssueId}")
    public void deleteBookIssueEntry(@PathParam("bookIssueId") int bookIssueId) {

        new BookIssueService().removeBookIssueEntryFromDatabase(bookIssueId);
    }
}
