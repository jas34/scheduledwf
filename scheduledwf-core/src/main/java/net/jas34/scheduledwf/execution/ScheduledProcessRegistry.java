package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;

import java.util.List;

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
    boolean updateProcessStateById(ScheduledWorkFlow.State state, String id) throws IllegalStateException;

    boolean isProcessTobeScheduled(String name, String managerRefId);

    List<ScheduledWorkFlow> getTobeShutDownProcesses();

    void shutDownRegistry();
}
