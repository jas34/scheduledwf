package net.jas34.scheduledwf.scheduler;

import java.util.concurrent.CompletionStage;

/**
 * @author Jasbir Singh
 */
public class CronBasedScheduledProcess<Job> extends ScheduledProcess {

    private Job job;
    private CompletionStage<Job> shutdownReference;

    public CronBasedScheduledProcess(Job job) {
        this.job = job;
    }

    public void setShutdownReference(CompletionStage<Job> shutdownReference) {
        this.shutdownReference = shutdownReference;
    }

    @Override
    public Job getJobReference() {
        return job;
    }
}
