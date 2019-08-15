package com.learning.filters;

import com.learning.config.ProjectConfig;
import com.learning.dao.UserDAO;

import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

@Provider
@PreMatching
public class CorssFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    /**
     * Method for ContainerRequestFilter.
     */
    @Override
    public void filter(ContainerRequestContext request) throws IOException {

        List<String> authHeaders = request.getHeaders().get(AUTHORIZATION_HEADER_KEY);

        if(authHeaders != null && authHeaders.size() > 0) {
            String authToken = authHeaders.get(0);
            authToken = authToken.replaceAll(AUTHORIZATION_HEADER_PREFIX, "");
            byte[] decodedBytes = Base64.getDecoder().decode(authToken);
            String decodedString = new String(decodedBytes);
            StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
            String usernameRecieved = tokenizer.nextToken();
            String passwordRecieved = tokenizer.nextToken();

            if(usernameRecieved.equals(ProjectConfig.LIBRARY_ADMIN_USERNAME)) {
                boolean authenticationStatus = new UserDAO().authenticateUserFromDatabase(usernameRecieved, passwordRecieved);
                if (authenticationStatus)
                    return;
            }
        }

        Response unauthorizedStatus = Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("{ \"Error\" : \"User does not have access !!\" }")
                .build();

        request.abortWith(unauthorizedStatus);

        // If it's a preflight request, we abort the request with
        // a 200 status, and the CORS headers are added in the
        // response filter method below.
        if (isPreflightRequest(request)) {
            request.abortWith(Response.ok().build());
            return;
        }
    }

    /**
     * A preflight request is an OPTIONS request
     * with an Origin header.
     */
    private static boolean isPreflightRequest(ContainerRequestContext request) {
        return request.getHeaderString("Origin") != null
                && request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    /**
     * Method for ContainerResponseFilter.
     */
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response)
            throws IOException {

        MultivaluedMap<String, Object> headers = response.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        //headers.add("Access-Control-Allow-Origin", "http://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");



        /*// if there is no Origin header, then it is not a
        // cross origin request. We don't do anything.
        if (request.getHeaderString("Origin") == null) {
            return;
        }

        // If it is a preflight request, then we add all
        // the CORS headers here.
        if (isPreflightRequest(request)) {
//            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.getHeaders().add("Access-Control-Allow-Headers",
                    // Whatever other non-standard/safe headers (see list above)
                    // you want the client to be able to send to the server,
                    // put it in this list. And remove the ones you don't want.
                    "Content-Type, X-Requested-With, Accept, Accept-Encoding, Accept-Language, " +
                            "Origin, Access-Control-Request-Method, Access-Control-Request-Headers, " +
                            "Connection, Host, authorization");
        }

        // Cross origin requests can be either simple requests
        // or preflight request. We need to add this header
        // to both type of requests. Only preflight requests
        // need the previously added headers.
        response.getHeaders().add("Access-Control-Allow-Origin", "*");*/
    }
}
