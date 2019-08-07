package com.learning.webresource;

import com.learning.dto.User;
import com.learning.dto.UserIssuedBook;
import com.learning.service.BookIssueService;
import com.learning.service.UserService;
import com.learning.webresource.filterbeans.UserFilterBean;
import com.learning.webresource.filterbeans.UserIssuedBookFilterBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static com.learning.utils.CommonUtils.*;

@Path("/users")
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@BeanParam UserFilterBean filterBean, @Context UriInfo uriInfo) {

        List<User> users = new UserService().getUsers(filterBean.getLastName(), filterBean.getOffset(), filterBean.getLimit());

        for(User user : users) {
            String userLink = getURISelf(uriInfo, user.getusername()).toString();
            user.addLink(userLink, "self");
        }

        return users;
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserDetails(@PathParam("username") String username, @Context UriInfo uriInfo) {

        User user = new UserService().getUserDetails(username);
        String userLink = getURISelf(uriInfo, user.getusername()).toString();
        user.addLink(userLink, "self");

        return user;
    }

    @PUT
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUserDetails(User user,
                                  @PathParam("username") String username,
                                  @Context UriInfo uriInfo) {

        user = new UserService().updateUserDetails(username, user);
        String userLink = getURISelf(uriInfo, user.getusername()).toString();
        user.addLink(userLink, "self");

        return user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewUser(User user, @Context UriInfo uriInfo) {

        User newUser = new UserService().addNewUser(user);
        URI userURI = getURISelf(uriInfo, user.getusername());
        newUser.addLink(userURI.toString(), "self");
        Response response = Response.created(userURI)
                .entity(newUser)
                .build();

        return response;
    }

    @DELETE
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("username") String username) {

        new UserService().deleteUser(username);
    }

    @GET
    @Path("/{username}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserIssuedBook> getUserIssuedBooks(@PathParam("username") String username,
                                                   @BeanParam UserIssuedBookFilterBean bean, @Context UriInfo uriInfo) {

        List<UserIssuedBook> booksIssued = new BookIssueService().getListOfBooksIssuedToAUser(username, bean);

        return booksIssued;
    }
}
