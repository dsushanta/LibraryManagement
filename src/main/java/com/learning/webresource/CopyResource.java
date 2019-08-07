package com.learning.webresource;

import com.learning.dto.Copy;
import com.learning.service.CopyService;
import com.learning.webresource.filterbeans.CopyFilterBean;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static com.learning.utils.CommonUtils.*;

public class CopyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Copy> getCopies(@BeanParam CopyFilterBean copyBean, @Context UriInfo uriInfo) {

        List<Copy> copies = new CopyService().getCopies(copyBean);

        for(Copy copy : copies) {
            String copyLink = getURISelf(uriInfo, Integer.toString(copy.getCopyId())).toString();
            copy.addLink(copyLink, "self");
        }

        return copies;
    }

    @GET
    @Path("/{copyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Copy getCopyDetails(@PathParam("copyId") int copyId, @Context UriInfo uriInfo) {

        Copy copy = new CopyService().getCopyDetails(copyId);
        String copyLink = getURISelf(uriInfo, Integer.toString(copy.getCopyId())).toString();
        copy.addLink(copyLink, "self");

        return copy;
    }

    @DELETE
    @Path("/{copyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteCopy(@PathParam("copyId") int copyId) {

        new CopyService().deleteCopyFromDatabase(copyId);
    }

}
