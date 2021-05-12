package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public interface WorkflowSchedulerFactory<T extends ScheduledProcess> {

    WorkflowScheduler<T> getWorkflowSchedulerFactory(ScheduledWorkFlow scheduledWorkFlow);
}
