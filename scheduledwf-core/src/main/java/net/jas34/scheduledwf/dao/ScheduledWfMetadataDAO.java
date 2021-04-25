package net.jas34.scheduledwf.dao;

import net.jas34.scheduledwf.metadata.ScheduleWfDef;

import java.util.List;
import java.util.Optional;

/**
 * @author Jasbir Singh
 */
public interface ScheduledWfMetadataDAO {

    void saveScheduleWorkflow(ScheduleWfDef def);

    void updateScheduleWorkflow(ScheduleWfDef def);

    boolean removeScheduleWorkflow(String name);

    boolean removeScheduleWorkflows(List<String> names);

    Optional<ScheduleWfDef> getScheduledWorkflowDef(String name);

    Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status...status);

    Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefs();
}
