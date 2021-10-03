package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
public class TestScheduledTaskProvider implements ScheduledTaskProvider {

    private final IndexExecutionDataCallback callback;

    public TestScheduledTaskProvider(IndexExecutionDataCallback callback) {
        this.callback = callback;
    }

    @Override
    public Runnable getTask(ScheduledWorkFlow scheduledWorkFlow, SchedulerStats schedulerStats) {
        if (scheduledWorkFlow.getWfName().endsWith("sleep")) {
            return new TestTaskWithSleep(prepareScheduledTaskDef(scheduledWorkFlow), callback);
        }
        return new TestTask(prepareScheduledTaskDef(scheduledWorkFlow), callback);
    }

    private ScheduledTaskDef prepareScheduledTaskDef(ScheduledWorkFlow scheduledWorkFlow) {
        return new ScheduledTaskDef(scheduledWorkFlow.getName(), scheduledWorkFlow.getWfName(),
                scheduledWorkFlow.getWfVersion(), scheduledWorkFlow.getWfInput(), scheduledWorkFlow.getId(),
                scheduledWorkFlow.getManagerRefId());
    }
}
