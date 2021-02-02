package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;

/**
 * @author Jasbir Singh
 */
public interface WorkflowSchedulingAssistant {

    SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);

    SchedulingResult pauseSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);
}

