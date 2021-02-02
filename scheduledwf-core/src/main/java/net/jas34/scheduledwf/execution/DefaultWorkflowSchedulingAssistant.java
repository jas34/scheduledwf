package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.run.SchedulingResult;
import net.jas34.scheduledwf.run.ShutdownResult;

import java.util.List;

/**
 * @author Jasbir Singh
 */
public class DefaultWorkflowSchedulingAssistant implements WorkflowSchedulingAssistant {
    @Override
    public SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return null;
    }

    @Override
    public ShutdownResult shutdownSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow) {
        return null;
    }

    @Override
    public List<ShutdownResult> shutdownAllSchedulersWithFailSafety(List<ScheduledWorkFlow> scheduledWorkFlows) {
        return null;
    }
}
