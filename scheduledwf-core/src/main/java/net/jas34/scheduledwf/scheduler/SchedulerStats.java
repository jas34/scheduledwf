package net.jas34.scheduledwf.scheduler;

/**
 * @author Jasbir Singh
 */
public interface SchedulerStats {

    Long nextExecutionTimeInMillis(String scheduledWfName);

    Long lastExecutionStartedTimeInMillis(String scheduledWfName);

    Long lastExecutionEndedTimeInMillis(String scheduledWfName);
}
