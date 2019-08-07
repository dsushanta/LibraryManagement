package com.learning.webresource;

import com.learning.dto.UserIssuedBook;
import com.learning.service.BookIssueService;
import com.learning.webresource.filterbeans.UserIssuedBookFilterBean;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserIssuedBooksResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserIssuedBook> getUserIssuedBooks(@PathParam("username") String username,
                                                   @BeanParam UserIssuedBookFilterBean bean, @Context UriInfo uriInfo) {

        List<UserIssuedBook> booksIssued = new BookIssueService().getListOfBooksIssuedToAUser(username, bean);

        return booksIssued;
    }
}
