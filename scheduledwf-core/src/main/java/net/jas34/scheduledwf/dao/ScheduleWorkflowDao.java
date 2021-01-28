package net.jas34.scheduledwf.dao;

import net.jas34.scheduledwf.metadata.ScheduleWfDef;

import java.util.List;

/**
 * @author Jasbir Singh
 */
public interface ScheduleWorkflowDao {

    void scheduleWorkflow(ScheduleWfDef def);

    void updateScheduleWorkflow(ScheduleWfDef def);

    void removeScheduleWorkflow(String name);

    ScheduleWfDef getScheduledWorkflowDef(String name, int version);

    List<ScheduleWfDef> getAllScheduledWorkflowDefs();
}
