package net.jas34.scheduledwf.scheduler;

import com.netflix.conductor.common.metadata.Auditable;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.Status;
import net.jas34.scheduledwf.run.TriggerResult;

/**
 * @author Jasbir Singh
 */
public interface IndexExecutionDataCallback {

    void indexData(ScheduledTaskDef scheduledTaskDef, TriggerResult result);
}
