package com.learning.filters;

import com.learning.config.ProjectConfig;
import com.learning.dao.UserDAO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;


//@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        List<String> authHeaders = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

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

        requestContext.abortWith(unauthorizedStatus);
    }
}
