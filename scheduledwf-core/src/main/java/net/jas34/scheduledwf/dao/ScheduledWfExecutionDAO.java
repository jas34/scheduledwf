package net.jas34.scheduledwf.dao;

import java.util.List;
import java.util.Optional;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface ScheduledWfExecutionDAO {

    String createScheduledWorkflow(ScheduledWorkFlow scheduledWorkFlow);

    Optional<ScheduledWorkFlow> getScheduledWfWithNameAndMgrRefId(String name, String managerRefId);

    Optional<List<ScheduledWorkFlow>> getScheduledWfWithNamesAndMgrRefId(List<String> names, String managerRefId);

    Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithByManagerRefId(String managerRefId);

//    Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithStates(String managerRefId,
//            ScheduledWorkFlow.State... states);

    Optional<ScheduledWorkFlow> updateStateById(ScheduledWorkFlow.State state, String id, String name);

    void removeAllScheduledWorkflows(String managerRefId);
}
