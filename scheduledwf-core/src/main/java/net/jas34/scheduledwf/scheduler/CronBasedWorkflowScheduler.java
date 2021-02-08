package net.jas34.scheduledwf.scheduler;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.coreoz.wisp.Job;
import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedule;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class CronBasedWorkflowScheduler
        implements WorkflowScheduler<CronBasedScheduledProcess<Job>>, WorkflowJob {
    private Scheduler scheduler;
    private ScheduledTaskProvider taskProvider;

    @Inject
    public CronBasedWorkflowScheduler(Scheduler scheduler, ScheduledTaskProvider taskProvider) {
        this.scheduler = scheduler;
        this.taskProvider = taskProvider;
    }

    @Override
    public CronBasedScheduledProcess<Job> schedule(ScheduledWorkFlow scheduledWorkFlow) {
        Schedule schedule = CronSchedule.parseQuartzCron(scheduledWorkFlow.getCronExpression());
        Job scheduledJob = scheduler.schedule(scheduledWorkFlow.getName(),
                taskProvider.getTask(scheduledWorkFlow), schedule);
        return new CronBasedScheduledProcess<>(scheduledJob);
    }

    @Override
    public Void shutdown(CronBasedScheduledProcess<Job> scheduledProcess) {
        CompletionStage<Job> cancel = scheduler.cancel(scheduledProcess.getJobReference().name());
        scheduledProcess.setShutdownReference(cancel);
        return null;
    }

    @Override
    public Long nextExecutionTimeInMillis(String scheduledWfName) {
        return resolveJobFromName(scheduledWfName).nextExecutionTimeInMillis();
    }

    @Override
    public Long lastExecutionStartedTimeInMillis(String scheduledWfName) {
        return resolveJobFromName(scheduledWfName).lastExecutionStartedTimeInMillis();
    }

    @Override
    public Long lastExecutionEndedTimeInMillis(String scheduledWfName) {
        return resolveJobFromName(scheduledWfName).lastExecutionEndedTimeInMillis();
    }

    private Job resolveJobFromName(String scheduledWfName) {
        Optional<Job> job = scheduler.findJob(scheduledWfName);
        return job.orElse(null);
    }
// TODO: to shutdown scheduler itself
}
