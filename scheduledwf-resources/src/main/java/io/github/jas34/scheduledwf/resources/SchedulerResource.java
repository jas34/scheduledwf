package io.github.jas34.scheduledwf.resources;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.conductor.common.run.SearchResult;

import io.github.jas34.scheduledwf.run.ManagerInfo;
import io.github.jas34.scheduledwf.run.ScheduledWfExecData;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.service.SchedulerExecutionService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * This resource is meant to expose information about scheduled work flows and the running scheduled
 * jobs
 *
 * @author Jasbir Singh
 */
// @Api(value = "/scheduler", produces = MediaType.APPLICATION_JSON, consumes =
// MediaType.APPLICATION_JSON,
// tags = "Scheduler Management")
@RestController
@RequestMapping(value = "/api/scheduler")
public class SchedulerResource {

    private SchedulerExecutionService schedulerExecutionService;

    public SchedulerResource(SchedulerExecutionService schedulerExecutionService) {
        this.schedulerExecutionService = schedulerExecutionService;
    }

    @GetMapping("/managers")
    @Operation(summary = "Search for scheduler managers")
    public List<ManagerInfo> searchManagers(@RequestParam("nodeAddress") String nodeAddress) {
        return schedulerExecutionService.getManagerInfo(nodeAddress);
    }

    @GetMapping("/scheduled/workflows/")
    @Operation(
            summary = "Search for scheduled workflows based on name, or managerId or nodeAddress or schedulerId")
    public SearchResult<ScheduledWorkFlow> searchScheduledWorkFlow(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "managerId", required = false) String managerId,
            @RequestParam(name = "nodeAddress", required = false) String nodeAddress,
            @RequestParam(name = "schedulerId", required = false) String schedulerId,
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "size", defaultValue = "100") int size) {
        return schedulerExecutionService.searchScheduledWorkflow(name, managerId, nodeAddress, schedulerId,
                start, size);
    }

    @GetMapping("/scheduled/workflows/executions")
    @Operation(
            summary = "Search for scheduled workflows executions based on name, or managerId or nodeAddress or schedulerId")
    public SearchResult<ScheduledWfExecData> searchExecutions(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "managerId", required = false) String managerId,
            @RequestParam(name = "nodeAddress", required = false) String nodeAddress,
            @RequestParam(name = "schedulerId", required = false) String schedulerId,
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "size", defaultValue = "100") int size) {
        return schedulerExecutionService.searchScheduledWfExecData(name, managerId, nodeAddress, schedulerId,
                start, size);
    }
}
