package net.jas34.scheduledwf.execution;

import net.jas34.scheduledwf.metadata.ScheduleWfDef;

/**
 * @author Jasbir Singh
 */
public interface ScheduledProcessInputDataResolver<T> {

    T resolveInput(ScheduleWfDef wfDef);
}
