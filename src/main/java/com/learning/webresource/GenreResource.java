package com.learning.webresource;

import com.learning.dto.Genre;
import com.learning.service.BookService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class GenreResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Genre> getGenres() {

        List<Genre> genres = new BookService().getAllGenre();

        return genres;
    }
}
