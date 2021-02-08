package net.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultScheduledTaskProvider implements ScheduledTaskProvider {

    private final IndexExecutionDataCallback callback;

    @Inject
    public DefaultScheduledTaskProvider(IndexExecutionDataCallback callback) {
        this.callback = callback;
    }

    @Override
    public Runnable getTask(ScheduledWorkFlow scheduledWorkFlow) {
        return new TriggerScheduledWorkFlowTask(prepareScheduledTaskDef(scheduledWorkFlow), callback);
    }

    private ScheduledTaskDef prepareScheduledTaskDef(ScheduledWorkFlow scheduledWorkFlow) {
        return new ScheduledTaskDef(scheduledWorkFlow.getName(), scheduledWorkFlow.getWfName(),
                scheduledWorkFlow.getWfVersion(), scheduledWorkFlow.getWfInput(), scheduledWorkFlow.getId());
    }
}
