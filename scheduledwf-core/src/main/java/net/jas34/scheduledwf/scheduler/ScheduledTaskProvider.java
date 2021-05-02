package net.jas34.scheduledwf.scheduler;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface ScheduledTaskProvider {
    Runnable getTask(ScheduledWorkFlow scheduledWorkFlow, SchedulerStats schedulerStats);
}
