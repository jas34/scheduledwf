package net.jas34.scheduledwf.resources;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * Populates metadata definitions {@link ScheduleWfDef} of objects.
 *
 * @author Jasbir Singh
 */
@Api(value = "/scheduling/metadata", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,
        tags = "Scheduled Wofkflow Metadata Management")
@Path("/scheduling/metadata")
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
    @Path("/scheduleWf/{name}")
    @ApiOperation("Update status of a schedule workflow definition")
    public void update(@PathParam("name") String name, @QueryParam("status") ScheduleWfDef.Status status) {
        metadataService.updateScheduledWorkflowDef(name, status);
    }

    @GET
    @ApiOperation("Retrieves schedule workflow definition")
    @Path("/scheduleWf/{name}")
    public ScheduleWfDef get(@PathParam("name") String name) {
        return metadataService.getScheduledWorkflowDef(name);
    }

    @GET
    @ApiOperation("Retrieves all schedule workflow definitions")
    @Path("/scheduleWf")
    public List<ScheduleWfDef> getAll() {
        return metadataService.getScheduleWorkflowDefs();
    }

//    @DELETE
//    @Path("/scheduleWf/{name}")
//    @ApiOperation("Removes schedule workflow definition")
//    public void unregisterDef(@PathParam("name") String name) {
//        metadataService.unregisterScheduleWorkflowDef(name);
//    }
}
