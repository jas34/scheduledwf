package io.github.jas34.scheduledwf.execution;

import java.util.List;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.scheduler.ScheduledProcess;

/**
 * @author Jasbir Singh
 */
public interface ScheduledProcessRegistry {

    boolean addProcess(ScheduledWorkFlow scheduledWorkFlow);

    boolean updateProcessById(ScheduledProcess processReference, ScheduledWorkFlow.State state, String id,
            String name);

    boolean isProcessTobeScheduled(String name, String managerRefId);

    List<ScheduledWorkFlow> getTobeShutDownProcesses(String managerRefId, List<String> names);

    void removeProcess(ScheduledWorkFlow scheduledWorkFlow);

    List<ScheduledWorkFlow> getAllRunningProcesses(String managerRefId);

    void shutDownRegistry(String managerRefId, long waitTimeInMillis);
}
