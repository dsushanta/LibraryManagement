package com.learning.utils;

import com.learning.webresource.BookResource;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class CommonUtils {

    public static URI getURISelf(@Context UriInfo uriInfo, String id) {

        return uriInfo.getBaseUriBuilder()
                .path(BookResource.class)
                .path(id)
                .build();
    }
}
