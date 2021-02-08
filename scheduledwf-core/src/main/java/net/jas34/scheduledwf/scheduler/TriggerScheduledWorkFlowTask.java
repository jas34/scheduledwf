package net.jas34.scheduledwf.scheduler;

import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.Status;

/**
 * @author Jasbir Singh
 */
public class TriggerScheduledWorkFlowTask implements Runnable {

    private ScheduledTaskDef taskDef;

    private IndexExecutionDataCallback indexExecutionDataCallback;

    public TriggerScheduledWorkFlowTask(ScheduledTaskDef taskDef, IndexExecutionDataCallback indexExecutionDataCallback) {
        this.taskDef = taskDef;
        this.indexExecutionDataCallback = indexExecutionDataCallback;
    }

    @Override
    public void run() {
        //acquire lock
        System.out.println("I am running my task... look at me!");
        //start workflow

        //release lock
        indexExecutionDataCallback.indexData(taskDef, Status.SUCCESS, "");
    }
}
