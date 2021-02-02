package net.jas34.scheduledwf.run;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Jasbir Singh
 */
public class SchedulingResult {

    private String id;
    private long initialDelay;
    private long period;
    private TimeUnit timeUnit;
    private Status status;
    private ScheduledProcessReference processReference;
    private Exception exception;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ScheduledProcessReference getProcessReference() {
        return processReference;
    }

    public void setProcessReference(ScheduledProcessReference processReference) {
        this.processReference = processReference;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "SchedulingResult{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", exception=" + exception +
                '}';
    }

    public enum Status {
        SUCCESS,

        FAILURE;
    }
}
