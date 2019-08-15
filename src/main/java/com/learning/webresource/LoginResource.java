package com.learning.webresource;

import com.learning.dto.LoginResponse;
import com.learning.dto.User;
import com.learning.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/userlogin")
public class LoginResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse authenticateUser(User user) {

        boolean authenticationStatus = new UserService().authenticateUser(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthenticated(authenticationStatus);

        return loginResponse;
    }

    @OPTIONS
    @Produces(MediaType.APPLICATION_JSON)
    public Response optionsForBookResource() {
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Allow","POST, PUT, GET, DELETE, PATCH, OPTIONS")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .header("Content-Length", "0")
                .build();
    }

}
