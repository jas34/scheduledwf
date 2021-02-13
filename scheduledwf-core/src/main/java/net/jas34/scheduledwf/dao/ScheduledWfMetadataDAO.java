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

//    void removeScheduleWorkflow(String name);

    Optional<ScheduleWfDef> getScheduledWorkflowDef(String name, int version);

    Optional<List<ScheduleWfDef>> getAllScheduledWorkflowDefsByStatus(ScheduleWfDef.Status status);
}
