package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledProcessReference;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jasbir Singh
 */
public interface ScheduledProcessRegistry {

    void addProcess(ScheduledWorkFlow scheduledWorkFlow);

    /**
     *
     * @param state
     * @param id
     * @return
     * @throws IllegalStateException if state is not one of INITIALIZED, SCHEDULING_FAILED, RUNNING
     */
    boolean updateProcessById(ScheduledProcessReference<?> processReference, ScheduledWorkFlow.State state,
            String id) throws IllegalStateException;

    boolean isProcessTobeScheduled(String name, String managerRefId);

    List<ScheduledWorkFlow> getTobeShutDownProcesses(String managerRefId);

    void removeProcess(ScheduledWorkFlow scheduledWorkFlow);

    List<ScheduledWorkFlow> getAllRunningProcesses(String managerRefId);

    void shutDownRegistry(String managerRefId, long waitTimeInMillis);
}
