package io.github.jas34.scheduledwf.dao;

import java.util.List;
import java.util.Optional;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * This interface is meant to act as bridge between registry and persistence of scheduled workflows.
 * Serarchable data is expose via {@link IndexScheduledWfDAO}
 *
 * @author Jasbir Singh
 */
public interface ScheduledWfExecutionDAO {

    String createScheduledWorkflow(ScheduledWorkFlow scheduledWorkFlow);

    Optional<ScheduledWorkFlow> getScheduledWfWithNameAndMgrRefId(String name, String managerRefId);

    Optional<List<ScheduledWorkFlow>> getScheduledWfWithNamesAndMgrRefId(List<String> names,
            String managerRefId);

    Optional<List<ScheduledWorkFlow>> getAllScheduledWfWithByManagerRefId(String managerRefId);

    Optional<ScheduledWorkFlow> updateStateById(ScheduledWorkFlow.State state, String id, String name);

    void removeScheduledWorkflow(String name, String managerRefId);

    void removeAllScheduledWorkflows(String managerRefId);
}
