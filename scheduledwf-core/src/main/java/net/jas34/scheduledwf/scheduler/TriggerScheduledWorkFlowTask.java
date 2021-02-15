package net.jas34.scheduledwf.scheduler;

import com.netflix.conductor.service.WorkflowService;
import net.jas34.scheduledwf.metadata.ScheduledTaskDef;
import net.jas34.scheduledwf.run.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jasbir Singh
 */
public class TriggerScheduledWorkFlowTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(TriggerScheduledWorkFlowTask.class);

    private ScheduledTaskDef taskDef;

    private IndexExecutionDataCallback indexExecutionDataCallback;

    private WorkflowService workflowService;

    public TriggerScheduledWorkFlowTask(ScheduledTaskDef taskDef,
            IndexExecutionDataCallback indexExecutionDataCallback, WorkflowService workflowService) {
        this.taskDef = taskDef;
        this.indexExecutionDataCallback = indexExecutionDataCallback;
        this.workflowService = workflowService;
    }

    @Override
    public void run() {
        // TODO: acquire lock

        // start workflow
        String failureReason = null;
        String workflowId = null;
        try {
            //TODO: we can add throttling here by looking at number of running of workflows .... Let revisit later

            logger.debug("Going to start workflow with name={}", taskDef.getName());
            workflowId = workflowService.startWorkflow(taskDef.getWfName(), taskDef.getWfVersion(),
                    taskDef.getSchedulerId(), taskDef.getInput());
            logger.debug("Workflow with name={} started and workflowId={}", taskDef.getName(), workflowId);

        } catch (Exception e) {
            failureReason = e.getMessage() + ", cause: " + e.getCause();
            logger.error("Unable to start workflow due to error.", e);
        }

        // TODO: release lock
        indexExecutionDataCallback.indexData(taskDef, workflowId, Status.SUCCESS, failureReason);
    }
}
