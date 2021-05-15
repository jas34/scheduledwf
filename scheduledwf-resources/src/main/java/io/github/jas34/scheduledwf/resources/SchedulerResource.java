package io.github.jas34.scheduledwf.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.netflix.conductor.common.run.SearchResult;

import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWfExecData;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.service.SchedulerExecutionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This resource is meant to expose information about scheduled work flows and the running scheduled
 * jobs
 *
 * @author Jasbir Singh
 */
@Api(value = "/scheduler", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,
        tags = "Scheduler Management")
@Path("/scheduler")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class SchedulerResource {

    private SchedulerExecutionService schedulerExecutionService;

    @Inject
    public SchedulerResource(SchedulerExecutionService schedulerExecutionService) {
        this.schedulerExecutionService = schedulerExecutionService;
    }

    @GET
    @ApiOperation(value = "Search for scheduler managers")
    @Path("/managers")
    public List<ManagerInfo> searchManagers(@QueryParam("nodeAddress") String nodeAddress) {
        return schedulerExecutionService.getManagerInfo(nodeAddress);
    }

    @GET
    @ApiOperation(
            value = "Search for scheduled workflows based on name, or managerId or nodeAddress or schedulerId")
    @Path("/scheduled/workflows/")
    public SearchResult<ScheduledWorkFlow> searchScheduledWorkFlow(@QueryParam("name") String name,
            @QueryParam("managerId") String managerId, @QueryParam("nodeAddress") String nodeAddress,
            @QueryParam("schedulerId") String schedulerId, @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("size") @DefaultValue("100") int size) {
        return schedulerExecutionService.searchScheduledWorkflow(name, managerId, nodeAddress, schedulerId,
                start, size);
    }

    @GET
    @ApiOperation("Search for scheduled workflows executions based on name, or managerId or nodeAddress or schedulerId")
    @Path("/scheduled/workflows/executions")
    public SearchResult<ScheduledWfExecData> searchExecutions(@QueryParam("name") String name,
            @QueryParam("managerId") String managerId, @QueryParam("nodeAddress") String nodeAddress,
            @QueryParam("schedulerId") String schedulerId, @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("size") @DefaultValue("100") int size) {
        return schedulerExecutionService.searchScheduledWfExecData(name, managerId, nodeAddress, schedulerId,
                start, size);
    }
}
