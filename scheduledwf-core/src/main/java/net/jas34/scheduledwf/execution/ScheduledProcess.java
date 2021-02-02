package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface ScheduledProcess<T> {

    T run(ScheduledWorkFlow scheduledWorkFlow);
}
