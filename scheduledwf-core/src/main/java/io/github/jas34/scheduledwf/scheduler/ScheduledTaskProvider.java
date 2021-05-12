package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface ScheduledTaskProvider {
    Runnable getTask(ScheduledWorkFlow scheduledWorkFlow, SchedulerStats schedulerStats);
}
