package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public class DefaultWorkflowSchedulerFactory implements WorkflowSchedulerFactory<ScheduledProcess> {

    // As of now we have only cron based scheduling Hence factory will return only
    private CronBasedWorkflowScheduler cronBasedWorkflowScheduler;

    public DefaultWorkflowSchedulerFactory(CronBasedWorkflowScheduler cronBasedWorkflowScheduler) {
        this.cronBasedWorkflowScheduler = cronBasedWorkflowScheduler;
    }

    @Override
    public WorkflowScheduler<ScheduledProcess> getWorkflowSchedulerFactory(
            ScheduledWorkFlow scheduledWorkFlow) {
        return (WorkflowScheduler) cronBasedWorkflowScheduler;
    }
}
