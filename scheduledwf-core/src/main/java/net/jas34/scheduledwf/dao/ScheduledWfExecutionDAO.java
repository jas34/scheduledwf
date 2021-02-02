package net.jas34.scheduledwf.dao;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Jasbir Singh
 */
public interface ScheduledWfExecutionDAO {

    String createScheduledWorkflow(ScheduledWorkFlow scheduledWorkFlow);

    Optional<ScheduledWorkFlow> getScheduledWfWithNameAndMgrRefId(String name, String managerRefId);

    Optional<ScheduledWorkFlow> getScheduledWfNotWithState(String name, String managerRefId, ScheduledWorkFlow.State... states);

    ScheduledWorkFlow updateStateById(ScheduledWorkFlow.State state, String id);

    Optional<List<ScheduledWorkFlow>> getAllTobeShutDownScheduledWf();

    ScheduledWorkFlow updateNextRunAtById(String nextRunAt, String id);
}
