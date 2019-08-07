package com.learning.exception.ExceptionMappers;

import com.learning.dto.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

//@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        String link = "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500, link);

        Response errorResponse = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();

        return errorResponse;
    }
}
