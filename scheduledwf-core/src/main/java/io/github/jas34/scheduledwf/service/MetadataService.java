package io.github.jas34.scheduledwf.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.github.jas34.scheduledwf.metadata.ScheduleWfDef;

import org.springframework.validation.annotation.Validated;

/**
 * @author Jasbir Singh
 */
@Validated
public interface MetadataService {

    void registerScheduleWorkflowDef(
            @NotNull(message = "ScheduleWfDef cannot be null") @Valid ScheduleWfDef def);

    void updateScheduledWorkflowDef(@NotEmpty(message = "Workflow name cannot be null or empty") String name,
            @NotNull(message = "scheduling status cannot be null or empty") ScheduleWfDef.Status status);

    ScheduleWfDef getScheduledWorkflowDef(
            @NotEmpty(message = "Workflow name cannot be null or empty") String name);

    List<ScheduleWfDef> getScheduleWorkflowDefs();

    void unregisterScheduleWorkflowDef(
            @NotEmpty(message = "Workflow name cannot be null or empty") String name);
}
