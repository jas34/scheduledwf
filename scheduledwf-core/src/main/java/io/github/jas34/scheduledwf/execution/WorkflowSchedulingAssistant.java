package io.github.jas34.scheduledwf.execution;

import java.util.List;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;
import io.github.jas34.scheduledwf.run.SchedulingResult;
import io.github.jas34.scheduledwf.run.ShutdownResult;

/**
 * @author Jasbir Singh
 */
public interface WorkflowSchedulingAssistant {

    SchedulingResult scheduleSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);

    ShutdownResult shutdownSchedulerWithFailSafety(ScheduledWorkFlow scheduledWorkFlow);

    List<ShutdownResult> shutdownAllSchedulersWithFailSafety(List<ScheduledWorkFlow> scheduledWorkFlows);
}

