package net.jas34.scheduledwf.run;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Jasbir Singh
 */
public class ScheduledProcessReference<T> {

    private ScheduledFuture<T> scheduledFuture;

    public ScheduledFuture<T> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<T> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
}
