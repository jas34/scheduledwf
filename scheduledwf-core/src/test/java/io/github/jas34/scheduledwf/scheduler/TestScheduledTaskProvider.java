package io.github.jas34.scheduledwf.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.run.ScheduledWorkFlow;

/**
 * @author Jasbir Singh
 */
@Singleton
public class TestScheduledTaskProvider implements ScheduledTaskProvider {

    private final IndexExecutionDataCallback callback;

    @Inject
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
