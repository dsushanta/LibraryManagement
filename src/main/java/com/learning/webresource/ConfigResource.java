package com.learning.webresource;

import com.learning.dto.LibraryConfig;
import com.learning.service.ConfigService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @GET
    public LibraryConfig getConfigurations() {
        return new ConfigService().getLibraryConfigurations();
    }

    @PUT
    public void updateConfigurations(LibraryConfig config) {
        new ConfigService().updateLibraryConfigurations(config);
    }
}
