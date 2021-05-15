package io.github.jas34.scheduledwf.scheduler;

import io.github.jas34.scheduledwf.metadata.ScheduledTaskDef;
import io.github.jas34.scheduledwf.run.Status;
import io.github.jas34.scheduledwf.run.TriggerResult;

/**
 * @author Jasbir Singh
 */
public class TestTask implements Runnable {

    private ScheduledTaskDef taskDef;

    private IndexExecutionDataCallback indexExecutionDataCallback;

    public TestTask(ScheduledTaskDef taskDef, IndexExecutionDataCallback indexExecutionDataCallback) {
        this.taskDef = taskDef;
        this.indexExecutionDataCallback = indexExecutionDataCallback;
    }

    @Override
    public void run() {
        System.out.println("I am running a test task for testing. Is this fine with you!");
        indexExecutionDataCallback.indexData(taskDef,
                new TriggerResult("test_trigger", Status.SUCCESS, null, null));
    }
}
