package io.github.jas34.scheduledwf.run;

import io.github.jas34.scheduledwf.scheduler.ScheduledProcess;

/**
 * @author Jasbir Singh
 */
public class SchedulingResult extends Result {

    private ScheduledProcess processReference;

    public SchedulingResult(String id) {
        super(id);
    }

    public ScheduledProcess getProcessReference() {
        return processReference;
    }

    public void setProcessReference(ScheduledProcess processReference) {
        this.processReference = processReference;
    }
}
