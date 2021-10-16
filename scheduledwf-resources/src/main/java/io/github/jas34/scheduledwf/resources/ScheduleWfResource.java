package io.github.jas34.scheduledwf.resources;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jas34.scheduledwf.metadata.ScheduleWfDef;
import io.github.jas34.scheduledwf.service.MetadataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * Populates metadata definitions {@link ScheduleWfDef} of objects.
 *
 * @author Jasbir Singh
 */
// @Api(value = "/scheduling/metadata", produces = MediaType.APPLICATION_JSON,
// consumes = MediaType.APPLICATION_JSON, tags = "Scheduled Worfkflow Metadata Management")
@RestController
@RequestMapping(value = "/api/scheduling/metadata")
public class ScheduleWfResource {

    private MetadataService metadataService;

    public ScheduleWfResource(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @PostMapping("/scheduleWf")
    @Operation(summary = "Schedule a new workflow")
    public void create(@RequestBody ScheduleWfDef scheduleWfDef) {
        metadataService.registerScheduleWorkflowDef(scheduleWfDef);
    }

    @PutMapping("/scheduleWf/{name}")
    @Operation(summary = "Update status of a schedule workflow definition.")
    public void update(@Parameter(description = "Scheduled workflow name.") @PathVariable("name") String name,
            @Parameter(
                    description = "status=SHUTDOWN/DELETE are equal. Either of them can be used.") @RequestParam("status") ScheduleWfDef.Status status) {
        metadataService.updateScheduledWorkflowDef(name, status);
    }

    @GetMapping("/scheduleWf/{name}")
    @Operation(summary = "Retrieves schedule workflow definition")
    public ScheduleWfDef get(@PathVariable("name") String name) {
        return metadataService.getScheduledWorkflowDef(name);
    }

    @GetMapping("/scheduleWf")
    @Operation(summary = "Retrieves all schedule workflow definitions")
    public List<ScheduleWfDef> getAll() {
        return metadataService.getScheduleWorkflowDefs();
    }
}
