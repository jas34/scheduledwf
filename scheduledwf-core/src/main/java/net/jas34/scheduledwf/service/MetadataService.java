package net.jas34.scheduledwf.service;

import net.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
public interface MetadataService {

    void registerScheduleWorkflowDef(ScheduleWfDef def);

    void updateScheduledWorkflowDef(ScheduleWfDef def);

    ScheduleWfDef getScheduledWorkflowDef(String name, int version);
}
