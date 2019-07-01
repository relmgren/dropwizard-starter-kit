package thotornot.ports.incoming.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thotornot.application.ThotOrNotService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Api("Thot Or Not Api")
@Path("/thot")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ThotResource {

    private static final Logger LOG = LoggerFactory.getLogger(ThotResource.class.getName());
    private final ThotOrNotService service;
    private final ObjectMapper objectMapper;

    public ThotResource(ThotOrNotService service, ObjectMapper objectMapper) {

        this.service = service;
        this.objectMapper = objectMapper;
    }

    @POST
    @PermitAll
    public Response post(@ApiParam(required = true) String payload) {
        LOG.info("Got post request {}", payload);
        try {
            PayloadDto payloadDto = objectMapper.readValue(payload, PayloadDto.class);
            service.handle(payloadDto);
            return Response.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
}
