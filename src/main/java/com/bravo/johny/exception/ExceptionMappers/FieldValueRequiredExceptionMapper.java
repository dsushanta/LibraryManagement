package com.bravo.johny.exception.ExceptionMappers;

import com.bravo.johny.dto.ErrorMessage;
import com.bravo.johny.exception.FieldValueRequiredException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FieldValueRequiredExceptionMapper implements ExceptionMapper<FieldValueRequiredException> {

    @Override
    public Response toResponse(FieldValueRequiredException ex) {
        String link = "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes";
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400, link);

        Response errorResponse = Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();

        return errorResponse;
    }
}
