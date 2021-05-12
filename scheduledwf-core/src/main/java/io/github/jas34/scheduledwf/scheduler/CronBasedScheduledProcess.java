package io.github.jas34.scheduledwf.scheduler;

/**
 * @author Jasbir Singh
 */
public class CronBasedScheduledProcess<Job> extends ScheduledProcess {

    private Job job;

    public CronBasedScheduledProcess(Job job) {
        this.job = job;
    }

    @Override
    public Job getJobReference() {
        return job;
    }
}
