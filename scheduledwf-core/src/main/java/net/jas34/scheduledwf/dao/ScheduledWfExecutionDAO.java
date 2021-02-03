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

    Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithByManagerRefId(String managerRefId);

    Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithStates(String managerRefId,
            ScheduledWorkFlow.State... states);

    ScheduledWorkFlow updateStateById(ScheduledWorkFlow.State state, String id);

    void removeAllScheduledWorkflows(String managerRefId);

    ScheduledWorkFlow updateNextRunAtById(String nextRunAt, String id);
}
