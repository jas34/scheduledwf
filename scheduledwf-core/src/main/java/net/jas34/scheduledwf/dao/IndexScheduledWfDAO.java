package net.jas34.scheduledwf.dao;

import net.jas34.scheduledwf.run.ScheduledWfExecData;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface IndexScheduledWfDAO {

    void indexCreatedScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow);

    void indexShutdownScheduledWorkFlow(ScheduledWorkFlow scheduledWorkFlow);

    void indexExecutedScheduledWorkflow(ScheduledWfExecData execData);
}
