package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;

/**
 * @author Jasbir Singh
 */
public class DefaultWorkflowSchedulingAssistant implements WorkflowSchedulingAssistant {

    @Override
    public SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return null;
    }

    @Override
    public SchedulingResult pauseSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return null;
    }
}
