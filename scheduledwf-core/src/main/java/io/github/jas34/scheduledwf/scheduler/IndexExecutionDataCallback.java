package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.run.TriggerResult;

/**
 * @author Jasbir Singh
 */
public interface IndexExecutionDataCallback {

    void indexData(ScheduledTaskDef scheduledTaskDef, TriggerResult result);
}
