package net.jas34.scheduledwf.execution;

import java.util.List;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.scheduler.ScheduledProcess;

/**
 * @author Jasbir Singh
 */
public interface ScheduledProcessRegistry {

    boolean addProcess(ScheduledWorkFlow scheduledWorkFlow);

    boolean updateProcessById(ScheduledProcess processReference, ScheduledWorkFlow.State state, String id,
            String name) throws IllegalStateException;

    boolean isProcessTobeScheduled(String name, String managerRefId);

    List<ScheduledWorkFlow> getTobeShutDownProcesses(String managerRefId);

    void removeProcess(ScheduledWorkFlow scheduledWorkFlow);

    List<ScheduledWorkFlow> getAllRunningProcesses(String managerRefId);

    void shutDownRegistry(String managerRefId, long waitTimeInMillis);
}
