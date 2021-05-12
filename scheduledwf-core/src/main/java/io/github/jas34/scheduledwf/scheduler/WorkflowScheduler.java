package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface WorkflowScheduler<T extends ScheduledProcess> {

    T schedule(ScheduledWorkFlow scheduledWorkFlow);

    Void shutdown(T scheduledProcess);
}
