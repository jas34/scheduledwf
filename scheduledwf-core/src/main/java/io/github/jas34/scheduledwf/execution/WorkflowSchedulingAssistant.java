package io.github.jas34.scheduledwf.execution;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.SchedulingResult;
import io.github.jas34.scheduledwf.run.ShutdownResult;

import java.util.List;

/**
 * @author Jasbir Singh
 */
public interface WorkflowSchedulingAssistant {

    SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);

    ShutdownResult shutdownSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);

    List<ShutdownResult> shutdownAllSchedulersWithFailSafety(List<ScheduledWorkFlow> scheduledWorkFlows);
}

