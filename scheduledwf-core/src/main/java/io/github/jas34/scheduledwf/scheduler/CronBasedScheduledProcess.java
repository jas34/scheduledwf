package io.github.jas34.scheduledwf.scheduler;

import com.coreoz.wisp.Job;

/**
 * @author Jasbir Singh
 */
public class CronBasedScheduledProcess extends ScheduledProcess<Job> {

    private Job job;

    public CronBasedScheduledProcess(Job job) {
        this.job = job;
    }

    @Override
    public Job getJobReference() {
        return job;
    }
}
