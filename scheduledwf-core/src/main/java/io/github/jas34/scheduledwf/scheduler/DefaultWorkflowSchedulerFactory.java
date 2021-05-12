package io.github.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultWorkflowSchedulerFactory implements WorkflowSchedulerFactory<ScheduledProcess> {

    //As of now we have only cron based scheduling Hence factory will return only CronBasedWorkflowScheduler
    private CronBasedWorkflowScheduler cronBasedWorkflowScheduler;

    @Inject
    public DefaultWorkflowSchedulerFactory(CronBasedWorkflowScheduler cronBasedWorkflowScheduler) {
        this.cronBasedWorkflowScheduler = cronBasedWorkflowScheduler;
    }

    @Override
    public WorkflowScheduler<ScheduledProcess> getWorkflowSchedulerFactory(ScheduledWorkFlow scheduledWorkFlow) {
        return (WorkflowScheduler)cronBasedWorkflowScheduler;
    }
}
