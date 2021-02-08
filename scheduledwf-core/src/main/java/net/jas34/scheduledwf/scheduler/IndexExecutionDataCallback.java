package net.jas34.scheduledwf.scheduler;

import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.Status;

/**
 * @author Jasbir Singh
 */
public interface IndexExecutionDataCallback {

    void indexData(ScheduledTaskDef scheduledTaskDef, Status status, String failureReason);
}
