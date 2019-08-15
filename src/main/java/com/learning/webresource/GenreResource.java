package com.learning.webresource;

import com.learning.dto.Genre;
import com.learning.service.BookService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

//@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GenreResource {

    @GET
    public List<Genre> getGenres() {

        List<Genre> genres = new BookService().getAllGenre();

        return genres;
    }
}
