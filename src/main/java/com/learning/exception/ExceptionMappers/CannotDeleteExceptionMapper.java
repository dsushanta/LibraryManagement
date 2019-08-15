package com.learning.exception.ExceptionMappers;

import com.learning.dto.ErrorMessage;
import com.learning.exception.CannotDeleteException;
import com.learning.exception.ValueAlreadyExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CannotDeleteExceptionMapper implements ExceptionMapper<CannotDeleteException> {

    @Override
    public Response toResponse(CannotDeleteException ex) {
        String link = "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400, link);

        Response errorResponse = Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();

        return errorResponse;
    }
}
