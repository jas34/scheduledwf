package net.jas34.scheduledwf.scheduler;

import java.time.Duration;
import java.util.Optional;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        implements WorkflowScheduler<CronBasedScheduledProcess<Job>>, SchedulerStats {
    private final Logger logger = LoggerFactory.getLogger(CronBasedWorkflowScheduler.class);

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
                taskProvider.getTask(scheduledWorkFlow, this), schedule);
        logger.info("scheduledWorkFlow scheduled with jobName={}", scheduledJob.name());
        return new CronBasedScheduledProcess<>(scheduledJob);
    }

    @Override
    public Void shutdown(CronBasedScheduledProcess<Job> scheduledProcess) {
        String jobName = scheduledProcess.getJobReference().name();
        scheduler.cancel(jobName);
        logger.info("scheduledWorkFlow with jobName={} cancelled", jobName);
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

    @PreDestroy
    public void shutDownScheduler() {
        logger.info("Going to shutdown cron scheduler");
        scheduler.gracefullyShutdown(Duration.ofMillis(1000));
        logger.info("Cron scheduler shutdown completed");
    }

    private Job resolveJobFromName(String scheduledWfName) {
        Optional<Job> job = scheduler.findJob(scheduledWfName);
        return job.orElse(null);
    }
}
