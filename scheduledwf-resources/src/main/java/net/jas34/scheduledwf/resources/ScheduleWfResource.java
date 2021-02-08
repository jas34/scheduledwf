package net.jas34.scheduledwf.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import net.jas34.scheduledwf.metadata.ScheduleWfDef;
import net.jas34.scheduledwf.service.MetadataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Jasbir Singh
 */
@Api(value = "/metadata", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,
        tags = "Scheduling Metadata Management")
@Path("/metadata")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ScheduleWfResource {

    private MetadataService metadataService;

    @Inject
    public ScheduleWfResource(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @POST
    @Path("/scheduleWf")
    @ApiOperation("Schedule a new workflow")
    public void create(ScheduleWfDef scheduleWfDef) {
        metadataService.registerScheduleWorkflowDef(scheduleWfDef);
    }

    @PUT
    @Path("/scheduleWf")
    @ApiOperation("Update a scheduled workflow definition")
    public void update(ScheduleWfDef scheduleWfDef) {
        metadataService.updateScheduledWorkflowDef(scheduleWfDef);
    }

    @GET
    @ApiOperation("Retrieves scheduled workflow definition")
    @Path("/scheduleWf/{name}")
    public ScheduleWfDef get(@PathParam("name") String name, @QueryParam("version") Integer version) {
        return metadataService.getScheduledWorkflowDef(name, version);
    }
}
