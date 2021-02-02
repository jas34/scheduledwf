package net.jas34.scheduledwf.run;

import java.util.concurrent.TimeUnit;

/**
 * @author Jasbir Singh
 */
public class SchedulingResult extends Result {

    private long initialDelay;
    private long period;
    private TimeUnit timeUnit;
    private ScheduledProcessReference processReference;

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public ScheduledProcessReference getProcessReference() {
        return processReference;
    }

    public void setProcessReference(ScheduledProcessReference processReference) {
        this.processReference = processReference;
    }
}
