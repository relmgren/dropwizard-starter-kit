package thotornot.ports.incoming.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.Managed;
import org.apache.log4j.Logger;
import thotornot.application.ThotOrNotService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.lang.String.format;

@Path("/thot/{thot}/{thot2}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThotResource implements Managed {

    private static final Logger LOG = Logger.getLogger(ThotResource.class.getName());
    private final ThotOrNotService service;
    private final ObjectMapper objectMapper;

    public ThotResource(ThotOrNotService service, ObjectMapper objectMapper) {

        this.service = service;
        this.objectMapper = objectMapper;
    }

    @POST
    public Response vote(@PathParam("thot") String thot, @PathParam("thot2") String secondThot, @QueryParam("vote") String wasThot) {
        LOG.info(format("Got post request for %s and %s, %s was the preferred", thot, secondThot, wasThot));


        service.handle(thot, secondThot, wasThot);

        return Response.ok().build();
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
    }
}
