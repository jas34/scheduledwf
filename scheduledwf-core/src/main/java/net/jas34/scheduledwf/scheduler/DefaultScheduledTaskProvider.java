package net.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.conductor.service.WorkflowService;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.ScheduledWorkFlow;
import net.jas34.scheduledwf.concurrent.LockingService;

/**
 * @author Jasbir Singh
 */
@Singleton
public class DefaultScheduledTaskProvider implements ScheduledTaskProvider {

    private final IndexExecutionDataCallback callback;

    private final WorkflowService workflowService;

    private LockingService lockingService;

    @Inject
    public DefaultScheduledTaskProvider(IndexExecutionDataCallback callback,
            WorkflowService workflowService, LockingService lockingService) {
        this.callback = callback;
        this.workflowService = workflowService;
        this.lockingService = lockingService;
    }

    @Override
    public Runnable getTask(ScheduledWorkFlow scheduledWorkFlow, SchedulerStats schedulerStats) {
        return new TriggerScheduledWorkFlowTask(prepareScheduledTaskDef(scheduledWorkFlow), callback,
                workflowService, lockingService, schedulerStats);
    }

    private ScheduledTaskDef prepareScheduledTaskDef(ScheduledWorkFlow scheduledWorkFlow) {
        return new ScheduledTaskDef(scheduledWorkFlow.getName(), scheduledWorkFlow.getWfName(),
                scheduledWorkFlow.getWfVersion(), scheduledWorkFlow.getWfInput(), scheduledWorkFlow.getId(),
                scheduledWorkFlow.getManagerRefId());
    }
}
